package me.acorneo.jobschedulerapi.exception;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Builder
@Jacksonized
public class ApiErrorResponse {
    private String message;
    private List<Map<String, Object>> details;
}
