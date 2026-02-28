package com.liftai.backend.services;

import com.liftai.backend.entities.User;
import com.liftai.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User syncKeycloakUser(final String keycloakId, final String email, final String name) {
        log.info("Sincronizando usuário do Keycloak ID: {}", keycloakId);

        return userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    log.info("Novo usuário detectado. Criando registro local para o email: {}", email);
                    final var newUser = User.builder()
                            .keycloakId(keycloakId)
                            .email(email)
                            .name(name)
                            .build();
                    return userRepository.save(newUser);
                });
    }
}