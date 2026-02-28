package com.liftai.backend.repositories;

import com.liftai.backend.entities.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, UUID> {

    List<Workout> findByUserId(UUID userId);
}