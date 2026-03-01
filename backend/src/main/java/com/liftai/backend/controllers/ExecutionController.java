package com.liftai.backend.controllers;

import com.liftai.backend.entities.SessionLog;
import com.liftai.backend.entities.WorkoutSession;
import com.liftai.backend.services.ExecutionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class ExecutionController {

    private final ExecutionService executionService;

    @PostMapping("/start")
    public ResponseEntity<WorkoutSession> startSession(@RequestHeader("User-Id") final UUID userId, @RequestBody(required = false) final StartSessionRequest request) {

        log.info("Recebida requisição para INICIAR sessão. Usuário: {}", userId);
        final var workoutId = (request != null) ? request.workoutId() : null;
        final var session = executionService.startSession(userId, workoutId);

        return ResponseEntity.ok(session);
    }

    @PostMapping("/{sessionId}/logs")
    public ResponseEntity<SessionLog> logExercise(@PathVariable final UUID sessionId, @RequestBody final LogExerciseRequest request) {

        log.info("Recebida requisição para REGISTRAR exercício na sessão: {}", sessionId);
        final var sessionLog = executionService.logExerciseExecution(
                sessionId,
                request.exerciseId(),
                request.sets(),
                request.reps(),
                request.weight(),
                request.notes()
        );

        return ResponseEntity.ok(sessionLog);
    }

    @PostMapping("/{sessionId}/finish")
    public ResponseEntity<WorkoutSession> finishSession(@PathVariable final UUID sessionId) {
        log.info("Recebida requisição para FINALIZAR sessão: {}", sessionId);
        final var finishedSession = executionService.finishSession(sessionId);
        return ResponseEntity.ok(finishedSession);
    }

    public record StartSessionRequest(UUID workoutId) {}

    public record LogExerciseRequest(
            UUID exerciseId,
            Integer sets,
            Integer reps,
            BigDecimal weight,
            String notes
    ) {}
}