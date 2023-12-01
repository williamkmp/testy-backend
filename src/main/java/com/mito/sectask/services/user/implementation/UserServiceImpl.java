package com.mito.sectask.services.user.implementation;

import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.encoder.PasswordEncocder;
import com.mito.sectask.services.user.UserService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncocder passwordEncoder;

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
    public Optional<UserEntity> updateUser(UserEntity userData) {
        boolean isUserExist = userRepository.existsById(userData.getId());
        if (!isUserExist) return Optional.empty();
        UserEntity updatedUser = userRepository.save(userData);
        return Optional.ofNullable(updatedUser);
    }

    @Override
    public Boolean validatePassword(Long userId, String password) {
        Optional<UserEntity> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Boolean.FALSE;
        }
        UserEntity registeredUser = maybeUser.get();
        return Boolean.valueOf(
            passwordEncoder.matches(password, registeredUser.getPassword())
        );
    }

    @Override
    public Boolean checkEmailIsAvailable(String email) {
        Optional<UserEntity> maybeDuplicate = userRepository.findByEmail(email);
        return Boolean.valueOf(maybeDuplicate.isEmpty());
    }

    @Override
    public Boolean checkEmailIsAvailable(String email, Long userId) {
        Optional<UserEntity> maybeDuplicate = userRepository.findByEmail(email);
        if (maybeDuplicate.isEmpty()) return Boolean.TRUE;
        UserEntity duplicate = maybeDuplicate.get();
        return !duplicate.getId().equals(userId);
    }

    @Override
    public Boolean checkTagNameIsAvailable(String tagName) {
        Optional<UserEntity> maybeDuplicate = userRepository.findByTagName(
            tagName
        );
        return Boolean.valueOf(maybeDuplicate.isEmpty());
    }

    @Override
    public Boolean checkTagNameIsAvailable(String tagName, Long userId) {
        Optional<UserEntity> maybeDuplicate = userRepository.findByTagName(
            tagName
        );
        if (maybeDuplicate.isEmpty()) return Boolean.TRUE;
        UserEntity duplicate = maybeDuplicate.get();
        return !duplicate.getId().equals(userId);
    }
}
