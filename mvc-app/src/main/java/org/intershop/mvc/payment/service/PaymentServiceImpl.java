package org.intershop.mvc.payment.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.payment.client.api.PaymentApi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentApi paymentApi;

    @Override
    public Mono<Void> processPayment(long totalAmount) {
        return paymentApi.paymentsPost(totalAmount)
                .then();
    }
}
