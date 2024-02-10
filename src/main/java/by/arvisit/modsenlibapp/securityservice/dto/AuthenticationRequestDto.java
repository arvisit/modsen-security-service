package by.arvisit.modsenlibapp.securityservice.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record AuthenticationRequestDto(
        @NotBlank
        @Length(max = 50, message = "Username should be no longer than {max} characters")
        String username,
        @NotBlank
        @Length(max = 72, message = "Password's length should be no longer than {max} characters")
        String password) {
}
