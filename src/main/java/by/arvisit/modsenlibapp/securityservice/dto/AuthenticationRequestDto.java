package by.arvisit.modsenlibapp.securityservice.dto;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record AuthenticationRequestDto(
        @NotBlank
        @Length(max = 50, message = "Username should be no longer than {max} characters")
        @Schema(description = "Username", example = "admin@mail.com")
        String username,
        @NotBlank
        @Length(max = 72, message = "Password's length should be no longer than {max} characters")
        @Schema(description = "Password", example = "admin")
        String password) {
}
