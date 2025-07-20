package io.github.deepaganesh.invoice.controller;

import io.github.deepaganesh.invoice.entity.Invoice;
import io.github.deepaganesh.invoice.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public Invoice createInvoice(@RequestBody Invoice invoice) {
        return invoiceService.createInvoice(invoice);
    }

    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    @PostMapping("/{id}/fund")
    public Invoice markFunded(@PathVariable Long id) {
        return invoiceService.markAsFunded(id);
    }
}
