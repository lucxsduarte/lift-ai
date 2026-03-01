package com.liftai.backend.controllers;

import com.liftai.backend.services.RoutineService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {

    private final RoutineService routineService;

    @PostMapping("/dynamic/activate")
    public ResponseEntity<Void> activateDynamicMode(@RequestHeader("User-Id") final UUID userId) {
        log.info("Recebida requisição para ATIVAR o modo dinâmico. Usuário: {}", userId);
        routineService.activateDynamicMode(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dynamic/deactivate")
    public ResponseEntity<Void> deactivateDynamicMode(@RequestHeader("User-Id") final UUID userId) {
        log.info("Recebida requisição para DESATIVAR o modo dinâmico. Usuário: {}", userId);
        routineService.deactivateDynamicMode(userId);
        return ResponseEntity.ok().build();
    }
}