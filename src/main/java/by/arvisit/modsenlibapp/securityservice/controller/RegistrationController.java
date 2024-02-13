package by.arvisit.modsenlibapp.securityservice.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import by.arvisit.modsenlibapp.securityservice.controller.openapi.RegistrationOpenApi;
import by.arvisit.modsenlibapp.securityservice.dto.RegistrationRequestDto;
import by.arvisit.modsenlibapp.securityservice.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/users/register")
public class RegistrationController implements RegistrationOpenApi {

    private final RegistrationService registrationService;

    @Override
    @PostMapping("/user")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid RegistrationRequestDto request) {
        String registeredUsername = registrationService.registerUser(request);
        Map<String, String> response = new HashMap<>();
        response.put("username", registeredUsername);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
