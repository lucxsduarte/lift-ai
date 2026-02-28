package com.liftai.backend.services;

import com.liftai.backend.entities.Routine;
import com.liftai.backend.entities.RoutineDay;
import com.liftai.backend.entities.User;
import com.liftai.backend.repositories.RoutineDayRepository;
import com.liftai.backend.repositories.RoutineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineDayRepository routineDayRepository;

    @Transactional
    public void activateDynamicMode(final UUID userId) {
        log.info("Iniciando ativação do modo dinâmico para o usuário ID: {}", userId);

        final var activeRoutineOpt = routineRepository.findByUserIdAndIsActiveTrue(userId);

        if (activeRoutineOpt.isEmpty()) {
            throw new RuntimeException("Usuário não possui um cronograma ativo para pausar.");
        }

        final var activeRoutine = activeRoutineOpt.get();

        final var dynamicRoutine = routineRepository.findByUserIdAndIsDynamicModeTrue(userId)
                .orElseGet(() -> createDynamicWorkspace(activeRoutine.getUser().getId()));

        dynamicRoutine.setPausedRoutineId(activeRoutine.getId());

        activeRoutine.setActive(false);
        dynamicRoutine.setActive(true);

        routineRepository.save(activeRoutine);
        routineRepository.save(dynamicRoutine);

        cloneRoutineDays(activeRoutine.getId(), dynamicRoutine.getId());

        log.info("Modo dinâmico ativado com sucesso. Rotina original pausada ID: {}", activeRoutine.getId());
    }

    @Transactional
    public void deactivateDynamicMode(final UUID userId) {
        log.info("Desativando modo dinâmico para o usuário ID: {}", userId);

        final var dynamicRoutine = routineRepository.findByUserIdAndIsDynamicModeTrue(userId)
                .orElseThrow(() -> new RuntimeException("Modo dinâmico não encontrado para este usuário."));

        if (!dynamicRoutine.isActive() || dynamicRoutine.getPausedRoutineId() == null) {
            throw new RuntimeException("O modo dinâmico não está ativo no momento.");
        }

        final var pausedRoutine = routineRepository.findById(dynamicRoutine.getPausedRoutineId())
                .orElseThrow(() -> new RuntimeException("Cronograma original não encontrado."));

        dynamicRoutine.setActive(false);
        dynamicRoutine.setPausedRoutineId(null);
        pausedRoutine.setActive(true);

        routineRepository.save(dynamicRoutine);
        routineRepository.save(pausedRoutine);

        final var dynamicDays = routineDayRepository.findByRoutineId(dynamicRoutine.getId());
        routineDayRepository.deleteAll(dynamicDays);

        log.info("Modo dinâmico desativado. Rotina original restaurada ID: {}", pausedRoutine.getId());
    }

    private Routine createDynamicWorkspace(final UUID userId) {
        final var userReference = User.builder().id(userId).build();

        final var workspace = Routine.builder()
                .user(userReference)
                .name("Dynamic Workspace")
                .isActive(false)
                .isDynamicMode(true)
                .build();

        return routineRepository.save(workspace);
    }

    private void cloneRoutineDays(final UUID sourceRoutineId, final UUID targetRoutineId) {
        final var oldTargetDays = routineDayRepository.findByRoutineId(targetRoutineId);
        routineDayRepository.deleteAll(oldTargetDays);

        final var sourceDays = routineDayRepository.findByRoutineId(sourceRoutineId);

        final var clonedDays = sourceDays.stream().map(sourceDay ->
                RoutineDay.builder()
                        .routine(Routine.builder().id(targetRoutineId).build())
                        .workout(sourceDay.getWorkout())
                        .dayOfWeek(sourceDay.getDayOfWeek())
                        .build()
        ).toList();

        routineDayRepository.saveAll(clonedDays);
    }
}