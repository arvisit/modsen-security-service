package by.arvisit.modsenlibapp.securityservice.controller;

import static by.arvisit.modsenlibapp.securityservice.util.RegistrationUtil.REGISTRATION_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import by.arvisit.modsenlibapp.exceptionhandlingstarter.exception.UsernameAlreadyExistsException;
import by.arvisit.modsenlibapp.exceptionhandlingstarter.handler.GlobalExceptionHandlerAdvice;
import by.arvisit.modsenlibapp.securityservice.dto.RegistrationRequestDto;
import by.arvisit.modsenlibapp.securityservice.filter.JwtFilter;
import by.arvisit.modsenlibapp.securityservice.service.RegistrationService;
import by.arvisit.modsenlibapp.securityservice.util.CommonRandomUtil;
import by.arvisit.modsenlibapp.securityservice.util.RegistrationUtil;

@WebMvcTest(controllers = RegistrationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class))
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerAdvice.class)
class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RegistrationService registrationService;

    @Test
    void shouldReturn201_when_registerUserWithValidCredentials() throws Exception {
        Mockito.when(registrationService.registerUser(Mockito.any())).thenReturn(Mockito.anyString());
        RegistrationRequestDto request = RegistrationUtil.createRegistrationRequestDto().build();

        mockMvc.perform(post(REGISTRATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn409_when_registerUserWithExistingUsername() throws Exception {
        Mockito.when(registrationService.registerUser(Mockito.any())).thenThrow(UsernameAlreadyExistsException.class);
        RegistrationRequestDto request = RegistrationUtil.createRegistrationRequestDto().build();

        mockMvc.perform(post(REGISTRATION_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Nested
    class InputValidation {

        @Test
        void shouldReturn400_when_registerUserWithWrongRePassword() throws Exception {
            RegistrationRequestDto request = RegistrationUtil.createRegistrationRequestDto()
                    .withPassword("password")
                    .withRePassword("repassword")
                    .build();
            mockMvc.perform(post(REGISTRATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void shouldReturn201_when_registerUserWithUsernameLengthEqualsTo50() throws Exception {
            Mockito.when(registrationService.registerUser(Mockito.any())).thenReturn(Mockito.anyString());
            RegistrationRequestDto request = RegistrationUtil.createRegistrationRequestDto()
                    .withUsername(CommonRandomUtil.generateRandomString(50))
                    .build();
            mockMvc.perform(post(REGISTRATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isCreated());
        }

        @Test
        void shouldReturn400_when_registerUserWithUsernameLengthGreaterThan50() throws Exception {
            RegistrationRequestDto request = RegistrationUtil.createRegistrationRequestDto()
                    .withUsername(CommonRandomUtil.generateRandomString(51))
                    .build();
            mockMvc.perform(post(REGISTRATION_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(request)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }
}
