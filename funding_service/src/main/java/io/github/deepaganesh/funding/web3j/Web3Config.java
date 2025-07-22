package io.github.deepaganesh.funding.web3j;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

import java.math.BigInteger;

@Configuration
@RequiredArgsConstructor
public class Web3Config {

    @Value("${app.web3.rpc-url}")
    private String rpcUrl;

    @Value("${app.web3.private-key}")
    private String privateKey;

    @Value("${app.web3.contract-address}")
    private String contractAddress;

    @Bean
    public Web3j web3j() {
        return Web3j.build(new HttpService(rpcUrl));
    }

    @Bean
    public Credentials credentials() {
        return Credentials.create(privateKey);
    }

    @Bean
    public ContractGasProvider contractGasProvider() {
        return new StaticGasProvider(
                BigInteger.valueOf(20000000000L), // gas price: 20 Gwei
                BigInteger.valueOf(6721975)       // gas limit: 5 million
        );
    }

    @Bean
    public InvoiceToken invoiceToken(Web3j web3j, Credentials credentials, ContractGasProvider gasProvider) {
        if (contractAddress == null || !contractAddress.startsWith("0x")) {
            throw new IllegalArgumentException("Invalid or missing contract address. Must be 0x-prefixed hex.");
        }
        return InvoiceToken.load(contractAddress, web3j, credentials, gasProvider);
    }
}
