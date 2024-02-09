package by.arvisit.modsenlibapp.securityservice;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class PostgreSQLTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.setProperty("spring.datasource.url", "jdbc:tc:postgresql:15-alpine:///");
        System.setProperty("spring.security.jwt.secret", "Jq2JJOJrmW1CDP7FaAHAuwT7LKYuW18J1YE8k5I4x0g9pHyr2gjv1UcuPATx_gQD");
        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        System.setProperty("spring.security.cors.allowedOrigin", "none");
        System.setProperty("spring.security.cors.allowedMethod", "none");
        System.setProperty("spring.security.cors.allowedHeader", "none");
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
    }

}
