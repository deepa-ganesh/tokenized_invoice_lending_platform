package io.github.deepaganesh.funding.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient webClient;

    @Value("${app.services.invoice-service.base-uri}")
    private String invoiceServiceBaseUri;

    @Value("${app.services.wallet-service.base-uri}")
    private String walletServiceBaseUri;

    @Value("${app.services.wallet-service.scheme}")
    private String walletServiceScheme;

    @Value("${app.services.wallet-service.host}")
    private String walletServiceHost;

    @Value("${app.services.wallet-service.port}")
    private String walletServicePort;

    public void markInvoiceFunded(Long invoiceId) {
        webClient.post()
                .uri(invoiceServiceBaseUri + "/api/invoices/" + invoiceId + "/fund")
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public void creditWallet(Long smeUserId, BigDecimal amount) {
        webClient.post()
                .uri(uriBuilder ->
                        uriBuilder
                                .scheme(walletServiceScheme)
                                .host(walletServiceHost)
                                .port(walletServicePort)
                                .path("/api/wallets/credit")
                                .queryParam("userId", smeUserId)
                                .queryParam("tokenSymbol", "INR")
                                .queryParam("amount", amount)
                                .build())
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    public String getWalletAddress(Long userId) {
        return webClient.get()
                .uri(walletServiceBaseUri + "/api/wallets/{userId}/address", userId)
                .retrieve()
                .bodyToMono(String.class)
                .block(); // Can be made reactive if needed
    }
}
