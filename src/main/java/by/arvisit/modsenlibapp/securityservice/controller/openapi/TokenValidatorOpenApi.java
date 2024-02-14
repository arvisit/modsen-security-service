package by.arvisit.modsenlibapp.securityservice.controller.openapi;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestHeader;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.response.ExceptionResponse;
import by.arvisit.modsenlibapp.securityservice.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "TokenValidator Controller", description = "API for access token validation")
public interface TokenValidatorOpenApi {

    String BAD_REQUEST = "BAD REQUEST";
    String UNAUTHORIZED = "UNAUTHORIZED";
    String OK = "OK";

    @Operation(
            summary = "Access token validation",
            description = "Validates the provided access token and returns user details",
            responses = {
                    @ApiResponse(responseCode = "200", description = OK,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDto.class))),
                    @ApiResponse(responseCode = "400", description = BAD_REQUEST,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class))),
                    @ApiResponse(responseCode = "401", description = UNAUTHORIZED,
                            content = @Content(mediaType = APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ExceptionResponse.class)))
            })
    UserDto validate(@RequestHeader(name = HttpHeaders.AUTHORIZATION) @Parameter(description = "Bearer token",
            example = "<token>") String authHeader);

}