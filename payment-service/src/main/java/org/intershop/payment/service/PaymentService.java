package org.intershop.payment.service;

import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<Void> processPayment(long amount);
}
