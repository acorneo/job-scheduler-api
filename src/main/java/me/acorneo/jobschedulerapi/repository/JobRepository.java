package me.acorneo.jobschedulerapi.repository;

import me.acorneo.jobschedulerapi.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
}
