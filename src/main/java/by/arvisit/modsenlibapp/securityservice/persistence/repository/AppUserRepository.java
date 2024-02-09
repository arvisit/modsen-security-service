package by.arvisit.modsenlibapp.securityservice.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import by.arvisit.modsenlibapp.securityservice.persistence.model.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);
}
