package me.acorneo.jobschedulerapi.controller;

import lombok.RequiredArgsConstructor;
import me.acorneo.jobschedulerapi.dto.HealthResponse;
import me.acorneo.jobschedulerapi.service.HealthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/health")
public class HealthController {
    private final HealthService healthService;

    @GetMapping("/ping")
    public ResponseEntity<HealthResponse> pong() {
        return ResponseEntity.status(HttpStatus.OK).body(
                healthService.pong()
        );
    }

    @GetMapping("/database")
    public ResponseEntity<HealthResponse> checkDatabase() {
        return ResponseEntity.status(HttpStatus.OK).body(
                healthService.checkDatabase()
        );
    }
}
