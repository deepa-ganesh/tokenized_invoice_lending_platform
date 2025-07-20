package io.github.deepaganesh.invoice.entity;

import io.github.deepaganesh.invoice.common.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String smeName;
    private String invoiceNumber;
    private BigDecimal amount;
    private String currency;

    private LocalDate issueDate;
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;
}
