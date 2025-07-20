package io.github.deepaganesh.wallet.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long walletId;

    private String tokenSymbol; // USDT, INR, ETH
    private BigDecimal balance;
}
