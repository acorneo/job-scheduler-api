package me.acorneo.jobschedulerapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

@Getter
@Builder
@Jacksonized
public class HealthResponse {
    private String message;
}
