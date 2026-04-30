package me.acorneo.jobschedulerapi.repository;

import lombok.NonNull;
import me.acorneo.jobschedulerapi.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    @Override
    @NonNull
    Page<Job> findAll(@NonNull Pageable pageable);

    @Modifying
    @Transactional
    @Query(value = "UPDATE job SET status = 'PROCESSING', attempts = attempts + 1, worker = :worker WHERE id = (SELECT id FROM job WHERE status = 'PENDING' ORDER BY created_at FOR UPDATE SKIP LOCKED LIMIT 1) RETURNING *;", nativeQuery = true)
    Optional<Job> findOneJob(@Param("worker") String worker);
}
