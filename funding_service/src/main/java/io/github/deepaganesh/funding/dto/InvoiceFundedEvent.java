package io.github.deepaganesh.funding.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class InvoiceFundedEvent {
    private Long invoiceId;
    private Long lpUserId;
    private Long smeUserId;
    private BigDecimal amount;
    private LocalDateTime fundedAt;
}
