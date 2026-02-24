package org.intershop.mvc.payment.service;

import lombok.RequiredArgsConstructor;
import org.intershop.mvc.payment.client.api.BalanceApi;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {
    private final BalanceApi balanceApi;

    @Override
    public Mono<Optional<Long>> getBalance() {
        return balanceApi.balanceGet()
                .map(Optional::of)
                .onErrorReturn(Optional.empty());
    }
}
