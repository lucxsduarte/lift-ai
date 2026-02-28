package com.liftai.backend.repositories;

import com.liftai.backend.entities.SessionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, UUID> {

    List<SessionLog> findBySessionId(UUID sessionId);

    Optional<SessionLog> findFirstBySessionUserIdAndExerciseIdOrderBySessionStartedAtDesc(UUID userId, UUID exerciseId);
}