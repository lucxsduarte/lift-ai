package com.liftai.backend.services;

import com.liftai.backend.entities.*;
import com.liftai.backend.repositories.SessionLogRepository;
import com.liftai.backend.repositories.WorkoutExerciseRepository;
import com.liftai.backend.repositories.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutionService {

    private final WorkoutSessionRepository sessionRepository;
    private final SessionLogRepository logRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    /**
     * Passo 1: O usuário clica em "Iniciar Treino".
     * Cria a sessão com a data de início e devolve para o app.
     */
    @Transactional
    public WorkoutSession startSession(final UUID userId, final UUID workoutId) {
        log.info("Iniciando nova sessão de treino para o usuário ID: {}", userId);

        // Verifica se já não existe um treino em andamento (Zombie Session)
        final var activeSession = sessionRepository.findByUserIdOrderByStartedAtDesc(userId).stream()
                .filter(s -> s.getFinishedAt() == null)
                .findFirst();

        if (activeSession.isPresent()) {
            log.warn("Usuário já possui um treino em andamento. Retornando a sessão existente.");
            return activeSession.get();
        }

        final var userRef = User.builder().id(userId).build();
        final var workoutRef = workoutId != null ? Workout.builder().id(workoutId).build() : null;

        final var newSession = WorkoutSession.builder()
                .user(userRef)
                .workout(workoutRef)
                .build();

        return sessionRepository.save(newSession);
    }

    /**
     * Passo 2: O usuário finaliza uma série (Set) no aplicativo.
     * Atualiza ou cria o registro de execução daquele exercício.
     */
    @Transactional
    public SessionLog logExerciseExecution(final UUID sessionId, final UUID exerciseId, final Integer sets, final Integer reps, final BigDecimal weight, final String notes) {
        log.info("Registrando execução do exercício ID: {} na sessão ID: {}", exerciseId, sessionId);

        final var sessionRef = WorkoutSession.builder().id(sessionId).build();
        final var exerciseRef = Exercise.builder().id(exerciseId).build();

        final var executionLog = SessionLog.builder()
                .session(sessionRef)
                .exercise(exerciseRef)
                .setsCompleted(sets)
                .repsCompleted(reps)
                .weightUsed(weight)
                .notes(notes)
                .build();

        return logRepository.save(executionLog);
    }

    /**
     * Passo 3: O usuário clica em "Finalizar Treino".
     * Registra o horário de fim e verifica se ele pulou algum exercício.
     */
    @Transactional
    public WorkoutSession finishSession(final UUID sessionId) {
        log.info("Finalizando sessão de treino ID: {}", sessionId);

        final var session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Sessão de treino não encontrada."));

        if (session.getFinishedAt() != null) {
            throw new RuntimeException("Esta sessão já foi finalizada anteriormente.");
        }

        session.setFinishedAt(LocalDateTime.now());

        // --- A SUA REGRA DE NEGÓCIO AQUI ---
        // Se a sessão estava atrelada a um Treino Planejado, vamos checar se ele fez tudo
        if (session.getWorkout() != null) {
            final var plannedExercises = workoutExerciseRepository.findByWorkoutIdOrderByOrderIndexAsc(session.getWorkout().getId());
            final var executedLogs = logRepository.findBySessionId(sessionId);

            if (executedLogs.size() < plannedExercises.size()) {
                int skipped = plannedExercises.size() - executedLogs.size();
                log.warn("Treino finalizado incompleto. O usuário pulou {} exercício(s).", skipped);
                // No futuro, podemos disparar um evento aqui para dar uma conquista "Feito é melhor que perfeito"
                // ou apenas devolver essa informação no JSON para o frontend pintar de amarelo.
            } else {
                log.info("Treino concluído com 100% de aproveitamento!");
            }
        }

        return sessionRepository.save(session);
    }

    /**
     * Passo 4: O "Faxineiro" (Cronjob).
     * Roda todos os dias às 03:00 da manhã para fechar treinos que o usuário
     * começou e esqueceu de clicar em finalizar no aplicativo.
     */
    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupZombieSessions() {
        log.info("Iniciando varredura de sessões zumbis (abandonadas)...");

        // Consideramos abandonado se começou há mais de 12 horas e não tem data de fim
        final var cutoffTime = LocalDateTime.now().minusHours(12);

        // Nota: Idealmente, criaríamos uma query no repositório para isso.
        // Para simplificar, estamos varrendo as sessões mais recentes.
        final var allSessions = sessionRepository.findAll();

        int closedCount = 0;
        for (WorkoutSession session : allSessions) {
            if (session.getFinishedAt() == null && session.getStartedAt().isBefore(cutoffTime)) {
                // Pega a hora do último exercício feito e adiciona 5 minutos, ou usa o limite de 2h se não fez nada
                session.setFinishedAt(session.getStartedAt().plusHours(2));
                sessionRepository.save(session);
                closedCount++;
            }
        }

        if (closedCount > 0) {
            log.info("Varredura concluída. {} sessões abandonadas foram fechadas automaticamente.", closedCount);
        } else {
            log.info("Varredura concluída. Nenhuma sessão zumbi encontrada.");
        }
    }
}