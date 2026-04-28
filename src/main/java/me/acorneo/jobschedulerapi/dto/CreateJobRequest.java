package me.acorneo.jobschedulerapi.dto;

import lombok.Data;
import me.acorneo.jobschedulerapi.enums.JobType;

@Data
public class CreateJobRequest {
    private JobType type;
    private String payload;
}
