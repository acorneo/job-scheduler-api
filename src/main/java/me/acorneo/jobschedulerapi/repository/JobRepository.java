package me.acorneo.jobschedulerapi.repository;

import lombok.NonNull;
import me.acorneo.jobschedulerapi.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    @Override
    @NonNull
    Page<Job> findAll(@NonNull Pageable pageable);

    @Query(value = "SELECT * FROM job WHERE status = :status ORDER BY created_at;", nativeQuery = true)
    List<Job> findOldestPendingJobsNative(@Param("status") String status);
}
