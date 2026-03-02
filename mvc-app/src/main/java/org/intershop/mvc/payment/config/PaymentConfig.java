package org.intershop.mvc.payment.config;

import org.intershop.mvc.payment.client.ApiClient;
import org.intershop.mvc.payment.client.api.BalanceApi;
import org.intershop.mvc.payment.client.api.PaymentApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    private final ApiClient apiClient;

    public PaymentConfig(@Value("${payment.api.base-url}") String baseUrl) {
        apiClient = new ApiClient();
        apiClient.setBasePath(baseUrl);
    }

    @Bean
    public BalanceApi balanceApi() {
        return new BalanceApi(apiClient);
    }

    @Bean
    public PaymentApi paymentApi() {
        return new PaymentApi(apiClient);
    }
}
