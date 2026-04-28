package me.acorneo.jobschedulerapi.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.enums.JobType;

import java.time.LocalDateTime;

@Builder
@Jacksonized
@Getter
public class CreateJobResponse {
    private Long id;
    private JobStatus status;
    private JobType type;
    private String payload;
    private Integer attempts;
    private LocalDateTime createdAt;
}
