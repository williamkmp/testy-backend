package com.mito.sectask.services.user.implementation;

import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.entities.UserEntityField;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.encoder.PasswordEncocder;
import com.mito.sectask.services.user.UserService;
import com.speedment.jpastreamer.application.JPAStreamer;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncocder passwordEncoder;
    private final JPAStreamer db;

    @Override
    @Transactional
    public Optional<UserEntity> registerUser(
        RegisterUserParameter newUserData
    ) {
        Optional<UserEntity> maybeUser;

        String encryptedPassword = passwordEncoder.encode(
            newUserData.getPassword()
        );
        UserEntity newUser = new UserEntity()
            .setEmail(newUserData.getEmail())
            .setTagName(newUserData.getTagName())
            .setFullName(newUserData.getFullName())
            .setPassword(encryptedPassword)
            .setIsDeleted(false);

        try {
            newUser = userRepository.save(newUser);
        } catch (Exception e) {
            newUser = null;
        }
        maybeUser = Optional.ofNullable(newUser);
        return maybeUser;
    }

    @Override
    public Optional<UserEntity> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Boolean checkEmailIsAvailable (String email) {
        Long duplicateEmail = db
            .stream(UserEntity.class)
            .filter(UserEntityField.email.equal(email))
            .count();
        return duplicateEmail <= 0;
    }

    @Override
    public Boolean checkEmailIsAvailable(String email, Long userId) {
        Long duplicateEmail = db
            .stream(UserEntity.class)
            .filter(UserEntityField.email.equal(email))
            .filter(UserEntityField.id.notEqual(userId))
            .count();
        return duplicateEmail <= 0;
    }

    @Override
    public Boolean checkTagNameIsAvailable(String tagName) {
        Long duplicateUsername = db
            .stream(UserEntity.class)
            .filter(UserEntityField.tagName.equal(tagName))
            .count();
        return duplicateUsername <= 0;
    }

    @Override
    public Boolean checkTagNameIsAvailable(String tagName, Long userId) {
        Long duplicateUsername = db
            .stream(UserEntity.class)
            .filter(UserEntityField.tagName.equal(tagName))
            .filter(UserEntityField.id.notEqual(userId))
            .count();
        return duplicateUsername <= 0;
    }
}
