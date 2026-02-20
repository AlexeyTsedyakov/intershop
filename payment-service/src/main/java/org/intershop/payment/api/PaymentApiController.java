package org.intershop.payment.api;

import lombok.RequiredArgsConstructor;
import org.intershop.payment.api.response.BaseResponseFactory;
import org.intershop.payment.model.BaseResponse;
import org.intershop.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@RequestMapping("${openapi.paymentService.base-path:/api}")
public class PaymentApiController implements PaymentApi {
    private final PaymentService paymentService;

    @Override
    public Mono<ResponseEntity<BaseResponse>> paymentsPost(Mono<Long> body, ServerWebExchange exchange) {
        return body
                .flatMap(paymentService::processPayment)
                .then(Mono.just(ResponseEntity.ok()
                        .body(BaseResponseFactory.success())));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Mono<ResponseEntity<BaseResponse>> handleIllegalArgument(IllegalArgumentException e) {
        return Mono.just(ResponseEntity.badRequest()
                .body(BaseResponseFactory.error(e.getMessage())));
    }

    @ExceptionHandler
    public Mono<ResponseEntity<BaseResponse>> handleGlobal(Exception e) {
        return Mono.just(ResponseEntity.internalServerError()
                .body(BaseResponseFactory.error(e.getMessage())));
    }
}
