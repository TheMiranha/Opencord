package com.miranda.opencord.user.infrastructure.service;

import com.miranda.opencord.user.domain.UserEntity;
import com.miranda.opencord.user.domain.exception.EmailInUse;
import com.miranda.opencord.user.domain.exception.UsernameInUse;
import com.miranda.opencord.user.infrastructure.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserEntity create(UserEntity user) {
        // NECESSARIO VALIDAR SE USERNAME JA EXISTE
        if (this.userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameInUse();
        }
        // NECESSARIO VALIDAR SE EMAIL JA EXISTE
        if (this.userRepository.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
            throw new EmailInUse();
        }

        return userRepository.save(user);
    }

}
