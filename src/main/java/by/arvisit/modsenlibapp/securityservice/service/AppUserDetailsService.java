package by.arvisit.modsenlibapp.securityservice.service;

import static org.springframework.security.core.userdetails.User.withUsername;

import java.text.MessageFormat;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import by.arvisit.modsenlibapp.securityservice.persistence.model.AppUser;
import by.arvisit.modsenlibapp.securityservice.persistence.model.RoleEnum;
import by.arvisit.modsenlibapp.securityservice.persistence.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(MessageFormat.format("Found no user with username: {0}", username)));
        boolean isDisabled = !user.getEnabled();

        return withUsername(user.getUsername())
                .password(user.getPassword())
                .disabled(isDisabled)
                .accountExpired(isDisabled)
                .accountLocked(isDisabled)
                .credentialsExpired(isDisabled)
                .roles(user.getRoles().stream()
                        .map(RoleEnum::name)
                        .toArray(String[]::new))
                .build();
    }

}
