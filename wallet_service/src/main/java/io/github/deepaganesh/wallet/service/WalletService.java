package io.github.deepaganesh.wallet.service;

import io.github.deepaganesh.wallet.entity.TokenBalance;
import io.github.deepaganesh.wallet.entity.Wallet;
import io.github.deepaganesh.wallet.repository.TokenBalanceRepository;
import io.github.deepaganesh.wallet.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final TokenBalanceRepository tokenBalanceRepository;

    @Value("${app.sme-wallet-address}")
    private String smeWalletAddress;

    public Wallet createWallet(Long userId) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        // Simulation
        ECKeyPair keyPair = Keys.createEcKeyPair();
        String walletAddress = "0x" + Keys.getAddress(keyPair.getPublicKey());

        Wallet wallet = Wallet.builder().userId(userId).address(smeWalletAddress).build();
        Wallet saved = walletRepository.save(wallet);

        tokenBalanceRepository.save(getTokenBalanceBySymbol(wallet.getId(), "USDT"));

        tokenBalanceRepository.save(getTokenBalanceBySymbol(wallet.getId(), "INR"));

        return saved;
    }

    public List<TokenBalance> getBalances(Long userId) {
        Optional<Wallet> walletOptional = walletRepository.findByUserId(userId);

        if (walletOptional.isPresent()) {
            return tokenBalanceRepository.findByWalletId(walletOptional.get().getId());
        }

        return Collections.emptyList();
    }

    public String getWalletAddress(Long userId) {
        Optional<Wallet> walletOptional = walletRepository.findByUserId(userId);

        return walletOptional.map(Wallet::getAddress).orElse(null);
    }

    public void creditBalance(Long userId, String tokenSymbol, BigDecimal amount) {
        Optional<Wallet> wallet = walletRepository.findByUserId(userId);

        if (wallet.isPresent()) {
            List<TokenBalance> balances = tokenBalanceRepository.findByWalletId(wallet.get().getId());

            for (TokenBalance b : balances) {
                if (b.getTokenSymbol().equalsIgnoreCase(tokenSymbol)) {
                    b.setBalance(b.getBalance().add(amount));
                    tokenBalanceRepository.save(b);
                    break;
                }
            }
        }
    }

    private static TokenBalance getTokenBalanceBySymbol(Long walletId, String tokenSymbol) {
        return TokenBalance.builder()
                .walletId(walletId)
                .tokenSymbol(tokenSymbol)
                .balance(new BigDecimal("0"))
                .build();
    }
}
