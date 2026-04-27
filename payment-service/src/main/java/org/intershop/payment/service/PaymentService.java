package org.intershop.payment.service;

import org.intershop.payment.model.PaymentRequest;
import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<Void> processPayment(PaymentRequest paymentRequest);
}
