package me.acorneo.jobschedulerapi.repository;

import lombok.NonNull;
import me.acorneo.jobschedulerapi.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {
    @Override
    @NonNull
    Page<Job> findAll(@NonNull Pageable pageable);
}
