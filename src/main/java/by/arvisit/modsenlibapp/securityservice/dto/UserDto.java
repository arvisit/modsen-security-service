package by.arvisit.modsenlibapp.securityservice.dto;

import java.util.Collection;

public record UserDto(String username, Collection<String> authorities) {

}
