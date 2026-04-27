package org.intershop.mvc.payment.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.payment.client.api.PaymentApi;
import org.intershop.mvc.payment.model.PaymentRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentApi paymentApi;

    @Override
    public Mono<Void> processPayment(long userId, long totalAmount) {
        var paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(userId);
        paymentRequest.setAmount(totalAmount);

        return paymentApi.paymentsPost(paymentRequest)
                .then();
    }
}
