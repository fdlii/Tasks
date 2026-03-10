package com.yourcompany.services;

import com.yourcompany.DTO.UserDTO;
import com.yourcompany.entities.RoleEntity;
import com.yourcompany.entities.UserEntity;
import com.yourcompany.repositories.RoleRepository;
import com.yourcompany.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtService jwtService;

    public void registerUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userDTO.getUsername());
        userEntity.setPassword(userDTO.getPassword());

        for (String role : userDTO.getRoles()) {
            Optional<RoleEntity> roleEntity = roleRepository.findByName(role);
            if (roleEntity.isEmpty()) {
                throw new IllegalArgumentException("Неопознанная роль.");
            }
            userEntity.addRole(roleEntity.get());
        }

        userRepository.save(userEntity);
    }

    public String verifyUser(UserDTO userDTO) throws UserPrincipalNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDTO.getUsername(),
                        userDTO.getPassword()
                ));
        if (authentication.isAuthenticated()) {
            UserDetails user = userRepository.findByUsername(userDTO.getUsername()).get();
            return jwtService.generateToken(user.getUsername(), user.getAuthorities());
        }
        throw new UserPrincipalNotFoundException("Неверное имя пользователя или пароль.");
    }
}
