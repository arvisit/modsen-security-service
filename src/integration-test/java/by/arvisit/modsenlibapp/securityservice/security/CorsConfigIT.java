package by.arvisit.modsenlibapp.securityservice.security;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import by.arvisit.modsenlibapp.securityservice.PostgreSQLTestContainerExtension;

@ActiveProfiles("itest")
@ExtendWith(PostgreSQLTestContainerExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class CorsConfigIT {

    private static final String PUBLIC_RESOURCE_URL = "/actuator/health";
    private static final String ORIGIN_HEADER = "http://evil.com";
    @Autowired
    private TestRestTemplate restTemplate;

    @DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
    @Nested
    class CorsAllRestricted {

        @BeforeAll
        static void setUp() {
            System.setProperty("spring.security.cors.allowedOrigin", "none");
            System.setProperty("spring.security.cors.allowedMethod", "none");
            System.setProperty("spring.security.cors.allowedHeader", "none");
        }

        @Test
        void should_return403AndValidMessage_when_accessPublicResourceAndCorsRestrictedAll() {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setOrigin(ORIGIN_HEADER);
            requestHeaders.setAccessControlRequestHeaders(List.of(HttpMethod.OPTIONS.name()));
            HttpEntity<String> request = new HttpEntity<>(requestHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    PUBLIC_RESOURCE_URL,
                    HttpMethod.OPTIONS,
                    request,
                    String.class);
            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            Assertions.assertThat(responseEntity.getBody()).isEqualTo("Invalid CORS request");
        }
    }

    @DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
    @Nested
    class CorsAllAllowed {

        @BeforeAll
        static void setUp() {
            System.setProperty("spring.security.cors.allowedOrigin", "*");
            System.setProperty("spring.security.cors.allowedMethod", "*");
            System.setProperty("spring.security.cors.allowedHeader", "*");
        }

        @Test
        void should_return200AndValidCorsHeaders_when_accessPublicResourceAndCorsAllowedAll() {
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setOrigin(ORIGIN_HEADER);
            requestHeaders.setAccessControlRequestHeaders(List.of(HttpMethod.OPTIONS.name()));
            HttpEntity<String> request = new HttpEntity<>(requestHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(
                    PUBLIC_RESOURCE_URL,
                    HttpMethod.OPTIONS,
                    request,
                    String.class);
            Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            Assertions.assertThat(responseEntity.getHeaders().getAccessControlAllowOrigin()).isEqualTo("*");
        }
    }
}
