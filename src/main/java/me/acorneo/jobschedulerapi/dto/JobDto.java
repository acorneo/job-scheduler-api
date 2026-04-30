package me.acorneo.jobschedulerapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.enums.JobType;

import java.time.LocalDateTime;

/**
 * This is basically Job DTO. It's used in both POST /jobs/ and in GET /jobs/{id}
 */
@Builder
@Jacksonized
@Getter
public class JobDto {
    private String id;
    private JobStatus status;
    private JobType type;
    private String payload;
    private Integer attempts;
    private LocalDateTime createdAt;
}
