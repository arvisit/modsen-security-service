package by.arvisit.modsenlibapp.securityservice.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.exception.BadRequestException;
import by.arvisit.modsenlibapp.exceptionhandlingstarter.exception.InvalidTokenException;
import by.arvisit.modsenlibapp.securityservice.dto.UserDto;
import by.arvisit.modsenlibapp.securityservice.service.JwtService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class TokenValidatorController {

    private final JwtService jwtService;
    
    @GetMapping("/validate")
    public UserDto validate(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authHeader) {
        if (!authHeader.startsWith("Bearer ")) {
            throw new BadRequestException("Invalid authentication header");
        }

        final String token = authHeader.split(" ")[1].trim();
        if (!jwtService.isValid(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        return jwtService.extractUser(token);
    }
    
}
