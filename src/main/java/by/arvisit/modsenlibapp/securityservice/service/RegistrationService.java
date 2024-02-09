package by.arvisit.modsenlibapp.securityservice.service;

import java.text.MessageFormat;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import by.arvisit.modsenlibapp.securityservice.dto.RegistrationRequestDto;
import by.arvisit.modsenlibapp.securityservice.persistence.model.AppUser;
import by.arvisit.modsenlibapp.securityservice.persistence.model.RoleEnum;
import by.arvisit.modsenlibapp.securityservice.persistence.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public String registerUser(RegistrationRequestDto credentials) {
        return register(credentials, RoleEnum.USER);
    }

    private String register(RegistrationRequestDto credentials, RoleEnum role) {
        String username = credentials.username();
        if (appUserRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(MessageFormat.format("Username {0} already exists", username));
        }
        AppUser userToCreate = AppUser.builder().withUsername(username)
                .withPassword(passwordEncoder.encode(credentials.password()))
                .withRoles(Set.of(role))
                .withEnabled(true)
                .build();
        return appUserRepository.save(userToCreate)
                .getUsername();
    }
}
