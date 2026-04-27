package org.intershop.payment.api;


import lombok.RequiredArgsConstructor;
import org.intershop.payment.api.response.BaseResponseFactory;
import org.intershop.payment.model.BaseResponse;
import org.intershop.payment.service.BalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Controller
@RequestMapping("${openapi.paymentService.base-path:/api}")
public class BalanceApiController implements BalanceApi {
    private final BalanceService balanceService;

    @Override
    public Mono<ResponseEntity<Long>> balanceGet(Long userId, ServerWebExchange exchange) {
        return balanceService.getBalance(userId)
                .map(ResponseEntity::ok);
    }

    @ExceptionHandler
    public Mono<ResponseEntity<BaseResponse>> handleException(Exception e) {
        return Mono.just(ResponseEntity.internalServerError()
                .body(BaseResponseFactory.error(e.getMessage())));
    }

}
