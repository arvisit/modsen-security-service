package by.arvisit.modsenlibapp.securityservice.util;

import by.arvisit.modsenlibapp.securityservice.dto.RegistrationRequestDto;

public final class RegistrationUtil {
    
    public static final String REGISTRATION_URL = "/api/v1/users/register/user";

    private RegistrationUtil() {
    }

    public static RegistrationRequestDto.RegistrationRequestDtoBuilder createRegistrationRequestDto() {
        return RegistrationRequestDto.builder()
                .withUsername("new_username@mail.com")
                .withPassword("password")
                .withRePassword("password");
    }
}
