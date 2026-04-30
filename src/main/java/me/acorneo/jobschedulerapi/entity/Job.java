package me.acorneo.jobschedulerapi.entity;

import lombok.Data;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.enums.JobType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@RedisHash("jobs")
public class Job {
    @Id
    private String id;

    @Indexed
    private JobStatus status;

    @Indexed
    private JobType type;

    private String payload;

    private String worker;

    private int attempts;

    private LocalDateTime createdAt;
}