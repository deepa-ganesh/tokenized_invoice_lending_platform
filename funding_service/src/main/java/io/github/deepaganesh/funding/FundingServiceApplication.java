package io.github.deepaganesh.funding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
@SpringBootApplication
public class FundingServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(FundingServiceApplication.class, args);
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
