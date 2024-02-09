package by.arvisit.modsenlibapp.securityservice.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import by.arvisit.modsenlibapp.securityservice.persistence.model.AppUser;
import by.arvisit.modsenlibapp.securityservice.persistence.repository.AppUserRepository;
import by.arvisit.modsenlibapp.securityservice.util.AppUserUtil;

@ExtendWith(MockitoExtension.class)
class AppUserDetailsServiceTest {

    @InjectMocks
    private AppUserDetailsService appUserDetailsService;

    @Mock
    private AppUserRepository appUserRepository;

    @Test
    void shouldReturnUserDetails_when_loadUserByUsernameWithValidUsername() {
        Optional<AppUser> user = Optional.of(AppUserUtil.createUser().build());

        Mockito.when(appUserRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        UserDetails result = appUserDetailsService.loadUserByUsername(AppUserUtil.USERNAME);

        Assertions.assertThat(result).isEqualTo(AppUserUtil.createUserDetails());
    }

    @Test
    void shouldThrowUsernameNotFoundException_when_loadUserByUsernameWithInvalidUsername() {
        Optional<AppUser> user = Optional.empty();

        Mockito.when(appUserRepository.findByUsername(Mockito.anyString())).thenReturn(user);

        Assertions.assertThatThrownBy(() -> appUserDetailsService.loadUserByUsername(AppUserUtil.USERNAME))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage(AppUserUtil.USERNAME_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}
