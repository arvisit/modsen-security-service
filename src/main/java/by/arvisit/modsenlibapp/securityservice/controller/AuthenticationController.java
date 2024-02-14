package by.arvisit.modsenlibapp.securityservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.modsenlibapp.securityservice.controller.openapi.AuthenticationOpenApi;
import by.arvisit.modsenlibapp.securityservice.dto.AuthenticationRequestDto;
import by.arvisit.modsenlibapp.securityservice.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users")
public class AuthenticationController implements AuthenticationOpenApi {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody @Valid AuthenticationRequestDto request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            UserDetails user = (UserDetails) authentication.getPrincipal();
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("username", request.username());
            return ResponseEntity.ok()
                    .header(HttpHeaders.AUTHORIZATION,
                            jwtService.generateAccessToken(user))
                    .body(responseBody);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
