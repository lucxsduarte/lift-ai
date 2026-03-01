package com.liftai.backend.controllers;

import com.liftai.backend.entities.SessionLog;
import com.liftai.backend.entities.WorkoutSession;
import com.liftai.backend.services.ExecutionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    public ResponseEntity<WorkoutSession> startSession(@RequestHeader("User-Id") final UUID userId, @Valid @RequestBody(required = false) final StartSessionRequest request) {

        log.info("Recebida requisição para INICIAR sessão. Usuário: {}", userId);
        final var workoutId = (request != null) ? request.workoutId() : null;
        final var session = executionService.startSession(userId, workoutId);

        return ResponseEntity.ok(session);
    }

    @PostMapping("/{sessionId}/logs")
    public ResponseEntity<SessionLog> logExercise(@PathVariable final UUID sessionId, @Valid @RequestBody final LogExerciseRequest request) {

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
            @NotNull(message = "O ID do exercício é obrigatório.")
            UUID exerciseId,

            @NotNull(message = "O número de séries é obrigatório.")
            @Min(value = 1, message = "Você deve ter completado pelo menos 1 série.")
            Integer sets,

            @NotNull(message = "O número de repetições é obrigatório.")
            @Min(value = 0, message = "O número de repetições não pode ser negativo.")
            Integer reps,

            @NotNull(message = "O peso utilizado é obrigatório.")
            @Min(value = 0, message = "O peso não pode ser negativo.")
            BigDecimal weight,

            String notes
    ) {}
}