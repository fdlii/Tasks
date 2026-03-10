package com.yourcompany.services;

import com.yourcompany.DTO.UserDTO;
import com.yourcompany.entities.RoleEntity;
import com.yourcompany.entities.UserEntity;
import com.yourcompany.repositories.RoleRepository;
import com.yourcompany.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

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

    public void verifyUser(UserDTO userDTO) {

    }
}
