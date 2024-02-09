package by.arvisit.modsenlibapp.securityservice.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/user")
    @RolesAllowed("USER")
    public String helloUser() {
        return "Hello, user";
    }

    @GetMapping("/admin")
    @RolesAllowed("ADMIN")
    public String helloAdmin() {
        return "Hello, admin";
    }

    @GetMapping("/service")
    @RolesAllowed("SERVICE")
    public String helloService() {
        return "Hello, service";
    }
}
