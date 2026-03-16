package com.yourcompany.services;

import com.yourcompany.DTO.UserDTO;
import com.yourcompany.entities.RoleEntity;
import com.yourcompany.entities.UserEntity;
import com.yourcompany.repositories.RoleRepository;
import com.yourcompany.repositories.UserRepository;
import com.yourcompany.utils.JwtHandler;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtHandler jwtHandler;
    @InjectMocks
    private UserService userService;

    //Data
    UserDTO userDTO;
    UserEntity user;
    RoleEntity role;
    Authentication authentication;

    @BeforeEach
    public void setUpData() {
        role = new RoleEntity(1L, "ADMIN");
        userDTO = new UserDTO("Nikita", "1234", Set.of(role.getName()));
        user = new UserEntity(null, "Nikita", "1234", Set.of(role));
        authentication = new Authentication() {
            boolean isAuthenticated = true;

            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getRoles();
            }
            @Override
            public @Nullable Object getCredentials() {
                return null;
            }
            @Override
            public @Nullable Object getDetails() {
                return null;
            }
            @Override
            public @Nullable Object getPrincipal() {
                return user;
            }
            @Override
            public boolean isAuthenticated() {
                return isAuthenticated;
            }
            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                this.isAuthenticated = isAuthenticated;
            }
            @Override
            public String getName() {
                return "Nikita";
            }
        };
    }

    @Nested
    class RegisterUserTestClass {
        @Test
        public void registerUserSuccessfullyTest() {
            //Given
            String roleName = "ADMIN";
            Optional<RoleEntity> optional = Optional.ofNullable(role);

            //When
            when(roleRepository.findByName(roleName)).thenReturn(optional);
            when(userRepository.save(user)).thenReturn(user);

            //Then
            UserDTO returned = userService.registerUser(userDTO);
            assertTrue(returned.getRoles().contains(roleName));
            assertEquals(user.getUsername(), returned.getUsername());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        public void registerUserExceptionTest() {
            //Given
            String roleName = "ANONYMOUS";
            userDTO.setRoles(Set.of(roleName));
            Optional<RoleEntity> optional = Optional.empty();

            //When
            when(roleRepository.findByName(roleName)).thenReturn(optional);

            //Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                userService.registerUser(userDTO);
            });
            assertEquals("Неопознанная роль.", exception.getMessage());
        }
    }

    @Nested
    class VerifyUserTestClass {
        @Test
        public void verifyUserSuccessfullyTest() {
            //Given
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzOTAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30";
            Optional<UserEntity> optional = Optional.of(user);

            //When
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
            when(userRepository.findByUsername(any(String.class))).thenReturn(optional);
            when(jwtHandler.generateToken(user.getUsername(), user.getAuthorities())).thenReturn(token);

            //Then
            String returned = assertDoesNotThrow(() -> userService.verifyUser(userDTO));
            assertEquals(token, returned);
            verify(userRepository, times(1)).findByUsername(any(String.class));
        }

        @Test
        public void verifyUserExceptionTest() {
            //Given
            Optional<UserEntity> optional = Optional.of(user);
            authentication.setAuthenticated(false);

            //When
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

            //Then
            UserPrincipalNotFoundException exception = assertThrows(UserPrincipalNotFoundException.class, () -> {
                userService.verifyUser(userDTO);
            });
            assertEquals("Неверное имя пользователя или пароль.", exception.getName());
        }
    }
}