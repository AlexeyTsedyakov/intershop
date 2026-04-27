package org.intershop.payment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intershop.payment.repository.BalanceRepository;
import org.intershop.payment.service.BalanceService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class BalanceServiceImpl implements BalanceService {
    private final BalanceRepository balanceRepository;

    @Override
    public Mono<Long> getBalance(long userId) {
        return balanceRepository.getBalance(userId);
    }

    @Override
    public Mono<Void> updateBalance(long userId, long balance) {
        if (balance < 0) {
            return Mono.error(new IllegalArgumentException("Баланс не может быть отрицательным"));
        }
        return balanceRepository.updateBalance(userId, balance);
    }
}
