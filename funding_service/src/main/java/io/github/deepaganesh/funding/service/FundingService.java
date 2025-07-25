package io.github.deepaganesh.funding.service;

import io.github.deepaganesh.funding.client.WebClientService;
import io.github.deepaganesh.funding.comon.MintStatus;
import io.github.deepaganesh.funding.dto.InvoiceFundedEvent;
import io.github.deepaganesh.funding.entity.FundingTransaction;
import io.github.deepaganesh.funding.eventing.EventPublisher;
import io.github.deepaganesh.funding.repository.FundingRepository;
import io.github.deepaganesh.funding.web3j.InvoiceToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FundingService {

    @Value("${app.web3.account-address}")
    private String accountAddress;

    private final FundingRepository fundingRepository;
    private final EventPublisher eventPublisher;
    private final WebClientService webClientService;

    private final InvoiceToken invoiceToken;

    public FundingTransaction fundInvoice(Long lpUserId, Long invoiceId, Long smeUserId, BigDecimal amount) {
        FundingTransaction tx = FundingTransaction.builder()
                .lpUserId(lpUserId)
                .invoiceId(invoiceId)
                .smeUserId(smeUserId)
                .amount(amount)
                .mintStatus(MintStatus.PENDING)
                .timestamp(LocalDateTime.now())
                .build();

        FundingTransaction saved = fundingRepository.save(tx);

        /* --- REST commands ----------------------------------- */
        // REST call to invoice-service to mark as FUNDED
        webClientService.markInvoiceFunded(invoiceId);

        // REST call to wallet-service to credit SME's wallet
        webClientService.creditWallet(smeUserId, amount);

        InvoiceFundedEvent event = InvoiceFundedEvent.builder()
                .invoiceId(invoiceId)
                .lpUserId(lpUserId)
                .smeUserId(smeUserId)
                .amount(amount)
                .fundedAt(LocalDateTime.now())
                .build();

        /* --- Kafka/Rabbit event ------------------------------ */
        eventPublisher.publishInvoiceFunded(event);

        /* --- Blockchain call --------------------------------- */
        try {
            String smeWalletAddress = webClientService.getWalletAddress(smeUserId);
            log.info("SME Wallet Address: {}", smeWalletAddress);

            mintInvoiceToken(accountAddress, amount);

            saved.setMintStatus(MintStatus.SUCCESS);
        } catch (Exception e) {
            log.error("Minting failed: {}", e.getMessage(), e);
            saved.setMintStatus(MintStatus.FAILED);
        } finally {
            saved = fundingRepository.save(saved);
        }

        return saved;
    }

    public List<FundingTransaction> getAll() {
        return fundingRepository.findAll();
    }

    public BigInteger getTokenBalance(String walletAddress) {
        try {
            BigInteger invoiceTokenBalance = invoiceToken.balanceOf(walletAddress).send();
            log.info("Invoice Token balance: {}", invoiceTokenBalance.toString());

            return invoiceTokenBalance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve token balance for the address: " + walletAddress, e);
        }
    }

    @Service
    @Slf4j
    public static class SchedulerService {

        @Autowired
        FundingRepository fundingRepository;

        @Autowired
        WebClientService webClientService;

        @Autowired
        InvoiceToken invoiceToken;

        @Value("${app.web3.account-address}")
        private String accountAddress;

        @Autowired
        FundingService fundingService;

        @Scheduled(fixedDelay = 60000) // every 60 seconds
        public void retryFailedMints() {
            List<FundingTransaction> failedTxs = fundingRepository.findByMintStatus(MintStatus.FAILED);

            for (FundingTransaction tx : failedTxs) {
                try {
                    String smeWalletAddress = webClientService.getWalletAddress(tx.getSmeUserId());

                    fundingService.mintInvoiceToken(smeWalletAddress, tx.getAmount());
                    log.info("Retry successful for invoice {}: {}", tx.getInvoiceId(), smeWalletAddress);

                    tx.setMintStatus(MintStatus.SUCCESS);
                    fundingRepository.save(tx);
                } catch (Exception e) {
                    log.error("Retry failed for invoice {}: {}", tx.getInvoiceId(), e.getMessage());
                    // still FAILED, can retry again
                }
            }
        }
    }

    private void mintInvoiceToken(String walletAddress, BigDecimal amount) throws Exception {
        log.info("Minting invoice token for wallet address: {} and amount: {}", walletAddress, amount);
        int tokenDecimals = 18;

        BigInteger tokenAmount = amount
                .multiply(BigDecimal.TEN.pow(tokenDecimals))
                .toBigIntegerExact();
        log.info("Converted token amount (wei): {}", tokenAmount);

        TransactionReceipt receipt = invoiceToken.mint(walletAddress, tokenAmount).send();
        log.info("Invoice token minted. TxHash = {}", receipt.getTransactionHash());
    }
}
