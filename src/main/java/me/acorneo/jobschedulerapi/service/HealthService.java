package me.acorneo.jobschedulerapi.service;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.dto.HealthResponse;
import me.acorneo.jobschedulerapi.exception.ServiceUnavailableException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final RedisConnectionFactory redisConnectionFactory;

    public HealthResponse pong() {
        return HealthResponse.builder()
                .message("Server online")
                .build();
    }

    public HealthResponse checkRedis() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            String pong = connection.ping();

            if (pong == null) {
                throw new ServiceUnavailableException("Redis down");
            }

            return HealthResponse.builder()
                    .message("Redis online")
                    .build();
        } catch (Exception e) {
            throw new ServiceUnavailableException("Redis down");
        }
    }

    // Backward-compatible alias.
    public HealthResponse checkDatabase() {
        return checkRedis();
    }
}
