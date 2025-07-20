package io.github.deepaganesh.invoice.service;

import io.github.deepaganesh.invoice.entity.Invoice;
import io.github.deepaganesh.invoice.common.InvoiceStatus;
import io.github.deepaganesh.invoice.repository.InvoiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice createInvoice(Invoice invoice) {
        invoice.setStatus(InvoiceStatus.PENDING);
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice markAsFunded(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElse(null);

        if (invoice != null) {
            invoice.setStatus(InvoiceStatus.FUNDED);
            return invoiceRepository.save(invoice);
        }

        return null;
    }
}
