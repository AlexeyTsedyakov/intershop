package org.intershop.mvc.payment.service;

import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<Void> processPayment(long totalAmount);
}
