package by.arvisit.modsenlibapp.securityservice.util;

import static org.springframework.security.core.userdetails.User.withUsername;

import java.text.MessageFormat;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import by.arvisit.modsenlibapp.securityservice.persistence.model.AppUser;
import by.arvisit.modsenlibapp.securityservice.persistence.model.RoleEnum;

public final class AppUserUtil {

    public static final String USERNAME = "username@mail.com";
    public static final String PASSWORD = "password";
    public static final boolean IS_DISABLED = false;
    public static final String USERNAME_NOT_FOUND_EXCEPTION_MESSAGE = MessageFormat.format("Found no user with username: {0}",
            USERNAME);
    public static final SimpleGrantedAuthority AUTHORITY_USER = new SimpleGrantedAuthority("ROLE_" + RoleEnum.USER.name());

    private AppUserUtil() {
    }

    public static AppUser.AppUserBuilder createUser() {
        return AppUser.builder()
                .withId(1L)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .withEnabled(IS_DISABLED)
                .withRoles(Set.of(RoleEnum.USER));
    }

    public static UserDetails createUserDetails() {
        return withUsername(USERNAME)
                .password(PASSWORD)
                .disabled(IS_DISABLED)
                .accountExpired(IS_DISABLED)
                .accountLocked(IS_DISABLED)
                .credentialsExpired(IS_DISABLED)
                .roles(RoleEnum.USER.name())
                .build();
    }
}
