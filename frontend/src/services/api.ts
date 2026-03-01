import axios from 'axios';

export const api = axios.create({
    // URL base do seu Spring Boot (se ele estiver rodando na 8081, basta alterar aqui)
    baseURL: 'http://localhost:8081/api',

    headers: {
        'Content-Type': 'application/json',

        // IMPORTANTE: Lembra que o seu Controller no Java exige o @RequestHeader("User-Id")?
        // Como nós pausamos o Keycloak, estamos mandando um UUID fixo (hardcoded)
        // temporariamente só para o backend não bloquear a requisição.
        'User-Id': 'cbd9be92-7f52-46ed-ac0b-bf06bafbd06b'
    }
});