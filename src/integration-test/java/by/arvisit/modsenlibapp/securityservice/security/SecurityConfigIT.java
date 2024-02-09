package by.arvisit.modsenlibapp.securityservice.security;

import static by.arvisit.modsenlibapp.securityservice.util.UrlConstants.AUTHENTICATION_URL;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.response.ExceptionResponse;
import by.arvisit.modsenlibapp.securityservice.PostgreSQLTestContainerExtension;
import by.arvisit.modsenlibapp.securityservice.dto.AuthenticationRequestDto;
import by.arvisit.modsenlibapp.securityservice.util.UrlConstants;

@ActiveProfiles("itest")
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@ExtendWith(PostgreSQLTestContainerExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@SqlGroup({
    @Sql(scripts = "classpath:sql/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
    @Sql(scripts = "classpath:sql/delete-users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) })
class SecurityConfigIT {
    private static final AuthenticationRequestDto USER_WITH_USER_ADMIN_AUTHORITIES = new AuthenticationRequestDto(
            "john_doe@mail.com", "password");
    private static final AuthenticationRequestDto USER_WITH_ADMIN_AUTHORITY = new AuthenticationRequestDto(
            "jane_doe@mail.com", "password");
    private static final AuthenticationRequestDto NOT_EXISTING_USER = new AuthenticationRequestDto(
            "not_a_user@mail.com", "password");
    private static final String USER_AUTHORITY_RESPONSE_BODY = "Hello, user";
    private static final String ADMIN_AUTHORITY_RESPONSE_BODY = "Hello, admin";

    private static final String MALFORMED_JWT_RESPONSE_MESSAGE = "Full authentication is required to access this resource";

    private static final MediaType TEXT_PLAIN_UTF8 = MediaType.valueOf("text/plain;charset=UTF-8");

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class AuthenticationWithCredentials {

        @Test
        void should_return200AndJsonContentType_when_authenticateWithValidCredentials() {
            AuthenticationRequestDto request = USER_WITH_USER_ADMIN_AUTHORITIES;
            HttpEntity<AuthenticationRequestDto> requestEntity = new HttpEntity<>(request);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    AUTHENTICATION_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(responseEntity.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
        }

        @Test
        void should_return200AndTokenInHeader_whenAuthenticateWithValidCredentials() {
            AuthenticationRequestDto request = USER_WITH_USER_ADMIN_AUTHORITIES;
            HttpEntity<AuthenticationRequestDto> requestEntity = new HttpEntity<>(request);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    AUTHENTICATION_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(responseEntity.getHeaders()).containsKey(HttpHeaders.AUTHORIZATION);
            Assertions.assertThat(responseEntity.getHeaders().get(HttpHeaders.AUTHORIZATION)).isNotNull();
        }

        @Test
        void should_return401_whenAuthenticateWithInvalidCredentials() {
            AuthenticationRequestDto request = NOT_EXISTING_USER;
            HttpEntity<AuthenticationRequestDto> requestEntity = new HttpEntity<>(request);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    AUTHENTICATION_URL,
                    HttpMethod.POST,
                    requestEntity,
                    String.class);

            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

    }

    @Nested
    class AccessWithJWT {

        @Test
        void shouldReturn200AndTextPlainContentType_whenRequestToUserAuthorityEndpointWithValidAuthorities() {
            HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_USER_ADMIN_AUTHORITIES));
            ResponseEntity<String> response = restTemplate.exchange(
                    UrlConstants.USER_ALLOWED_URL,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(TEXT_PLAIN_UTF8);
        }

        @Test
        void shouldReturn200AndResponse_whenRequestToUserAuthorityEndpointWithValidAuthorities() {
            HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_USER_ADMIN_AUTHORITIES));
            ResponseEntity<String> response = restTemplate.exchange(
                    UrlConstants.USER_ALLOWED_URL,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isEqualTo(USER_AUTHORITY_RESPONSE_BODY);
        }

        @Test
        void shouldReturn200AndTextPlainContentType_whenRequestToAdminAuthorityEndpointWithValidAuthorities() {
            HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_USER_ADMIN_AUTHORITIES));
            ResponseEntity<String> response = restTemplate.exchange(
                    UrlConstants.ADMIN_ALLOWED_URL,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(TEXT_PLAIN_UTF8);
        }

        @Test
        void shouldReturn200AndResponse_whenRequestToAdminAuthorityEndpointWithValidAuthorities() {
            HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_USER_ADMIN_AUTHORITIES));
            ResponseEntity<String> response = restTemplate.exchange(
                    UrlConstants.ADMIN_ALLOWED_URL,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(response.getBody()).isEqualTo(ADMIN_AUTHORITY_RESPONSE_BODY);
        }

        @Test
        void shouldReturn403_whenRequestToUserAuthorityEndpointWithInvalidAuthorities() {
            HttpEntity<String> requestEntity = new HttpEntity<>(getAuthHeader(USER_WITH_ADMIN_AUTHORITY));
            ResponseEntity<String> response = restTemplate.exchange(
                    UrlConstants.USER_ALLOWED_URL,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        void shouldReturn401AndCorrectResponse_whenAccessResourceWithMalformedToken() {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setBearerAuth("not.a.token");
            HttpEntity<Void> requestEntity = new HttpEntity<>(requestHeaders);
            ResponseEntity<ExceptionResponse> response = restTemplate.exchange(
                    UrlConstants.USER_ALLOWED_URL,
                    HttpMethod.GET,
                    requestEntity,
                    ExceptionResponse.class);

            ExceptionResponse result = response.getBody();
            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            Assertions.assertThat(result.status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
            Assertions.assertThat(result.message()).isEqualTo(MALFORMED_JWT_RESPONSE_MESSAGE);
        }

    }

    @Nested
    class AccessWithNoJWT {

        @Test
        void shouldReturn401_whenRequestToAuthenticatedEndpointWithNoToken() {
            HttpEntity<String> requestEntity = new HttpEntity<>(new HttpHeaders());
            ResponseEntity<String> response = restTemplate.exchange(
                    UrlConstants.USER_ALLOWED_URL,
                    HttpMethod.GET,
                    requestEntity,
                    String.class);

            Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        }
    }

    private HttpHeaders getAuthHeader(AuthenticationRequestDto request) {
        HttpEntity<AuthenticationRequestDto> requestEntity = new HttpEntity<>(request);
        ResponseEntity<String> response = restTemplate.exchange(
                AUTHENTICATION_URL,
                HttpMethod.POST,
                requestEntity,
                String.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(response.getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
        return headers;
    }
}
