package me.acorneo.jobschedulerapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import me.acorneo.jobschedulerapi.enums.JobType;

@Data
public class CreateJobRequest {
    @NotNull
    private JobType type;
    @NotBlank
    private String payload;
}
