package io.github.deepaganesh.invoice.repository;

import io.github.deepaganesh.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> { }
