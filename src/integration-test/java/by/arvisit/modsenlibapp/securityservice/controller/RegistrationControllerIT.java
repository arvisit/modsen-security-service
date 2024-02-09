package by.arvisit.modsenlibapp.securityservice.controller;

import static by.arvisit.modsenlibapp.securityservice.util.UrlConstants.REGISTER_USER_URL;

import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.modsenlibapp.securityservice.PostgreSQLTestContainerExtension;
import by.arvisit.modsenlibapp.securityservice.dto.RegistrationRequestDto;

@ActiveProfiles("itest")
@ExtendWith(PostgreSQLTestContainerExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SqlGroup({
    @Sql(scripts = "classpath:sql/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "classpath:sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class RegistrationControllerIT {

    private static final String USERNAME_KEY = "username";
    private static final String VALID_USERNAME = "new_username@mail.com";
    private static final RegistrationRequestDto VALID_REGISTRATION_CREDENTIALS = new RegistrationRequestDto(
            VALID_USERNAME, "password", "password");
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturn201AndJsonMediaType_when_registerUserWithValidCredentials() {
        RegistrationRequestDto request = VALID_REGISTRATION_CREDENTIALS;
        HttpEntity<RegistrationRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                REGISTER_USER_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

    }

    @Test
    void shouldReturn201AndCorrectUsername_when_registerUserWithValidCredentials() {
        RegistrationRequestDto request = VALID_REGISTRATION_CREDENTIALS;
        HttpEntity<RegistrationRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<Map<String, String>> responseEntity = restTemplate.exchange(
                REGISTER_USER_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

        Map<String, String> response = responseEntity.getBody();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response).containsEntry(USERNAME_KEY, VALID_USERNAME);

    }
}
