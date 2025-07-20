package io.github.deepaganesh.funding.eventing;

import io.github.deepaganesh.funding.dto.InvoiceFundedEvent;

public interface EventPublisher {
    void publishInvoiceFunded(InvoiceFundedEvent event);
}
