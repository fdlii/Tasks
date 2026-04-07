package com.yourcompany.security;

import com.yourcompany.entities.UserEntity;
import com.yourcompany.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userDetails = userRepository.findByUsername(username);
        if (userDetails.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден.");
        }
        return userDetails.get();
    }
}
