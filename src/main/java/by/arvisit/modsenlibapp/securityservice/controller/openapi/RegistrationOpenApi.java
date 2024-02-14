package by.arvisit.modsenlibapp.securityservice.controller.openapi;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.response.ExceptionResponse;
import by.arvisit.modsenlibapp.securityservice.dto.RegistrationRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Registration Controller", description = "API for user registration")
public interface RegistrationOpenApi {

    String CREATED = "CREATED";
    String BAD_REQUEST = "BAD REQUEST";

    @Operation(
            summary = "User registration",
            description = "Registers a new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = CREATED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    ResponseEntity<Map<String, String>> registerUser(
            @Parameter(name = "request", description = "User registration request",
                    required = true) @RequestBody @Valid RegistrationRequestDto request);

}