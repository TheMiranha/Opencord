package com.miranda.opencord.user.infrastructure.service;

import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.EmailInUse;
import com.miranda.opencord.user.domain.exception.UsernameInUse;
import com.miranda.opencord.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity create(UserEntity user) {
        return userRepository.save(user);
    }

    public Optional<UserEntity> findByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public Optional<UserEntity> findByEmailIgnoreCase(String email) {
        return this.userRepository.findByEmailIgnoreCase(email);
    }


}
