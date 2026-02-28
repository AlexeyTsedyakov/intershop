package org.intershop.mvc.shared.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import java.io.IOException;

@TestConfiguration
public class EmbeddedRedisConfig {
    private static RedisServer redisServer;

    @Value("${spring.data.redis.port:6379}")
    private int port;

    @PostConstruct
    public void init() throws IOException {
        if (!redisIsActive()) {
            redisServer = RedisServer.newRedisServer()
                    .port(port)
                    .build();
            redisServer.start();
        }
    }

    @PreDestroy
    public void destroy() throws IOException {
        if (redisIsActive()) {
            redisServer.stop();
        }
    }

    private boolean redisIsActive() {
        return redisServer != null && redisServer.isActive();
    }
}
