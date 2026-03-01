package com.liftai.backend.config;

import com.liftai.backend.entities.Exercise;
import com.liftai.backend.entities.User;
import com.liftai.backend.entities.Workout;
import com.liftai.backend.entities.WorkoutExercise;
import com.liftai.backend.repositories.ExerciseRepository;
import com.liftai.backend.repositories.UserRepository;
import com.liftai.backend.repositories.WorkoutExerciseRepository;
import com.liftai.backend.repositories.WorkoutRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutRepository workoutRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;

    @Override
    public void run(String... args) {
        log.info("Verificando se o banco de dados precisa ser populado...");

        // Verifica se já existem exercícios para não duplicar dados a cada vez que der o Play
        if (exerciseRepository.count() == 0) {
            log.info("Banco vazio detectado. Iniciando Database Seeder...");
            seedDatabase();
            log.info("Database Seeder finalizado com sucesso!");
        } else {
            log.info("O banco de dados já contém informações. Seeder ignorado.");
        }
    }

    private void seedDatabase() {
        // 1. Criar Catálogo de Exercícios
        final var benchPress = Exercise.builder().name("Supino Reto").category("Peito").description("Exercício base para peitoral com barra.").build();
        final var inclineDumbbell = Exercise.builder().name("Supino Inclinado com Halteres").category("Peito").description("Foco na parte superior do peitoral.").build();
        final var squat = Exercise.builder().name("Agachamento Livre").category("Pernas").description("Exercício completo para membros inferiores.").build();
        final var deadlift = Exercise.builder().name("Levantamento Terra").category("Costas").description("Trabalha a cadeia posterior completa.").build();
        final var pullUp = Exercise.builder().name("Barra Fixa").category("Costas").description("Excelente para largura das costas.").build();

        final var exercises = Arrays.asList(benchPress, inclineDumbbell, squat, deadlift, pullUp);
        exerciseRepository.saveAll(exercises);
        log.info("Catálogo de exercícios criado.");

        // 2. Criar Usuário de Teste Simulado
        final var testUser = User.builder()
                .keycloakId("simulated-keycloak-id-999")
                .email("teste@liftai.com")
                .name("Usuário de Teste")
                .build();
        userRepository.save(testUser);
        log.info("Usuário de teste criado. ID: {}", testUser.getId());

        // 3. Criar um Treino Base para o Usuário
        final var workoutA = Workout.builder()
                .user(testUser)
                .name("Treino A - Adaptação")
                .build();
        workoutRepository.save(workoutA);

        // 4. Adicionar Exercícios ao Treino Planejado (WorkoutExercise)
        final var item1 = WorkoutExercise.builder()
                .workout(workoutA)
                .exercise(benchPress)
                .targetSets(3)
                .targetReps(12)
                .baseWeight(new BigDecimal("20.0"))
                .orderIndex(1)
                .build();

        final var item2 = WorkoutExercise.builder()
                .workout(workoutA)
                .exercise(inclineDumbbell)
                .targetSets(3)
                .targetReps(10)
                .baseWeight(new BigDecimal("14.0"))
                .orderIndex(2)
                .build();

        workoutExerciseRepository.saveAll(Arrays.asList(item1, item2));
        log.info("Treino de teste 'Treino A - Adaptação' montado e vinculado ao usuário.");
    }
}