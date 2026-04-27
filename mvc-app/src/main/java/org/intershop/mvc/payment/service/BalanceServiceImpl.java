package org.intershop.mvc.payment.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.payment.client.api.BalanceApi;
import org.intershop.mvc.payment.model.BalanceResult;
import org.intershop.mvc.payment.model.BaseResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {
    private final BalanceApi balanceApi;

    @Override
    public Mono<BalanceResult> getBalance(Long userId) {
        Objects.requireNonNull(userId);

        return balanceApi.balanceGet(userId)
                .map(balance -> new BalanceResult(balance, null))
                .onErrorResume(WebClientResponseException.InternalServerError.class, e -> {
                    BaseResponse errorBody = e.getResponseBodyAs(BaseResponse.class);
                    String message = errorBody != null ?
                            errorBody.getMessage() : "Неизвестная ошибка сервера";
                    return Mono.just(new BalanceResult(null, message));
                })
                .onErrorResume(e -> Mono.just(new BalanceResult(null, e.getMessage())));
    }
}
