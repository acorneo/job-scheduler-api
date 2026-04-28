package me.acorneo.jobschedulerapi.service;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.dto.HealthResponse;
import me.acorneo.jobschedulerapi.exception.ServiceUnavailableException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthService {
    private final JdbcTemplate jdbcTemplate;

    public HealthResponse pong() {
        return HealthResponse.builder()
                .message("Server online")
                .build();
    }

    public HealthResponse checkDatabase() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            return HealthResponse.builder()
                    .message("Database online")
                    .build();
        } catch (Exception e) {
            throw new ServiceUnavailableException("Database down");
        }
    }
}
