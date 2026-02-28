package com.liftai.backend.repositories;

import com.liftai.backend.entities.RoutineDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoutineDayRepository extends JpaRepository<RoutineDay, UUID> {

    List<RoutineDay> findByRoutineId(UUID routineId);

    Optional<RoutineDay> findByRoutineIdAndDayOfWeek(UUID routineId, Integer dayOfWeek);
}