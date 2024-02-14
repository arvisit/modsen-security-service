package by.arvisit.modsenlibapp.securityservice.controller.openapi;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.response.ExceptionResponse;
import by.arvisit.modsenlibapp.securityservice.dto.AuthenticationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication Controller", description = "API for user authentication")
public interface AuthenticationOpenApi {

    String BAD_REQUEST = "BAD REQUEST";
    String UNAUTHORIZED = "UNAUTHORIZED";
    String OK = "OK";

    @Operation(
            summary = "User authentication",
            description = "Authenticates user and returns access token",
            responses = {
                    @ApiResponse(
                            responseCode = "200", description = OK,
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                            headers = @Header(name = AUTHORIZATION)),
                    @ApiResponse(
                            responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(
                            responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    ResponseEntity<Map<String, String>> authenticate(
            @Parameter(description = "User credentials",
                    required = true) @RequestBody @Valid AuthenticationRequestDto request);

}