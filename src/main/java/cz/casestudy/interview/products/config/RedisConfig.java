package cz.casestudy.interview.products.config;

import cz.casestudy.interview.products.model.BookingExpirationNotification;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.convert.KeyspaceConfiguration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import java.util.Collections;
import java.util.UUID;

/**
 * Redis configuration.
 */
@AllArgsConstructor
@Configuration
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP, keyspaceConfiguration = RedisConfig.MyKeyspaceConfiguration.class)
public class RedisConfig {

    @Bean
    public RedisTemplate<UUID, BookingExpirationNotification> getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<UUID, BookingExpirationNotification> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }


    public static class MyKeyspaceConfiguration extends KeyspaceConfiguration {

        @Override
        protected Iterable<KeyspaceSettings> initialConfiguration() {
            KeyspaceSettings keyspaceSettings = new KeyspaceSettings(BookingExpirationNotification.class, "booking");
            keyspaceSettings.setTimeToLive(10L);
            return Collections.singleton(keyspaceSettings);
        }

    }
}
