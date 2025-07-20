package io.github.deepaganesh.funding.entity;

import io.github.deepaganesh.funding.comon.MintStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FundingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long lpUserId;
    private Long invoiceId;
    private Long smeUserId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private MintStatus mintStatus;

    private LocalDateTime timestamp;
}
