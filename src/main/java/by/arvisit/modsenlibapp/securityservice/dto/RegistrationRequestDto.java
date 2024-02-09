package by.arvisit.modsenlibapp.securityservice.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(setterPrefix = "with")
public record RegistrationRequestDto(
        @NotBlank
        @Length(max = 50, message = "Username should be no longer than {max} characters")
        String username,
        @NotBlank
        @Length(min = 8, max = 72, message = "Password's length should be in range {min} - {max} characters")
        String password,
        @NotBlank
        @Length(min = 8, max = 72, message = "RePassword's length should be in range {min} - {max} characters")
        String rePassword
        ) {

    @AssertTrue(message = "Incorrect rePassword")
    private boolean isCorrectRePassword() {
        if (password != null && rePassword != null) {
            return password.equals(rePassword);
        }
        return true;
    }
}
