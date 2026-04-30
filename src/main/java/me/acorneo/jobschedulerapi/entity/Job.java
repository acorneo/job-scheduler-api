package me.acorneo.jobschedulerapi.entity;

import jakarta.persistence.*;
import lombok.Data;
import me.acorneo.jobschedulerapi.enums.JobStatus;
import me.acorneo.jobschedulerapi.enums.JobType;

import java.time.LocalDateTime;

@Data
@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private JobStatus status;

    @Enumerated(EnumType.STRING)
    private JobType type;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String worker;

    private int attempts;

    private LocalDateTime createdAt;
}
