package com.yourcompany.controllers;

import com.yourcompany.DTO.UserDTO;
import com.yourcompany.security.MyUserDetailsService;
import com.yourcompany.services.UserService;
import com.yourcompany.utils.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    JwtHandler jwtHandler;
    @MockitoBean
    MyUserDetailsService userDetailsService;

    private UserDTO userDTO;
    private UserDTO registeredUserDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setPassword("password123");
        userDTO.setRoles(Set.of("USER"));

        registeredUserDTO = new UserDTO();
        registeredUserDTO.setUsername("testuser");
        registeredUserDTO.setPassword("password123"); // обычно пароль не возвращается, но если DTO такой — оставляем
        registeredUserDTO.setRoles(Set.of("USER"));
    }

    // ====================== POST /user/register ======================
    @Nested
    class RegisterUserTestClass {

        @Test
        void registerUserSuccessfullyTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(userDTO);

            when(userService.registerUser(any(UserDTO.class))).thenReturn(registeredUserDTO);

            mockMvc.perform(post("/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.username").value("testuser"))
                    .andExpect(jsonPath("$.roles").isArray())
                    .andExpect(jsonPath("$.roles[0]").value("USER"));
        }

        @Test
        void registerUserIllegalArgumentTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(userDTO);

            doThrow(new IllegalArgumentException("Username already exists"))
                    .when(userService).registerUser(any(UserDTO.class));

            mockMvc.perform(post("/user/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }

    // ====================== POST /user/login ======================
    @Nested
    class LoginUserTestClass {

        @Test
        void loginUserSuccessfullyTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(userDTO);
            String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.fake-token.example";

            when(userService.verifyUser(any(UserDTO.class))).thenReturn(expectedToken);

            mockMvc.perform(post("/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isOk())
                    .andExpect(content().string(expectedToken));   // возвращается просто String (JWT токен)
        }

        @Test
        void loginUserUserPrincipalNotFoundTest() throws Exception {
            String requestJson = objectMapper.writeValueAsString(userDTO);

            doThrow(new UserPrincipalNotFoundException("User not found"))
                    .when(userService).verifyUser(any(UserDTO.class));

            mockMvc.perform(post("/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson))
                    .andExpect(status().isBadRequest());
        }
    }
}