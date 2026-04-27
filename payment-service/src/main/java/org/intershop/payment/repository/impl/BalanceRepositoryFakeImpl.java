package org.intershop.payment.repository.impl;

import org.intershop.payment.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Repository
public class BalanceRepositoryFakeImpl implements BalanceRepository {
    @Value("${config.init-balance:0}")
    private long initBalance;
    private final Map<Long, Long> balanceByUserId = new HashMap<>() ;

    @Override
    public Mono<Long> getBalance(long userId) {
        return Mono.just(balanceByUserId.computeIfAbsent(userId, id -> initBalance));
    }

    @Override
    public Mono<Void> updateBalance(long userId, long balance) {
        return Mono.fromRunnable(() -> balanceByUserId.put(userId, balance));
    }
}
