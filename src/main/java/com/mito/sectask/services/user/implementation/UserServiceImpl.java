package com.mito.sectask.services.user.implementation;

import com.mito.sectask.dto.parameters.RegisterUserParameter;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.User;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.encoder.PasswordEncocder;
import com.mito.sectask.services.user.UserService;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncocder passwordEncoder;
    private final PageRepository pageRepository;

    @Override
    @Transactional
    public Optional<User> registerUser(RegisterUserParameter newUserData) {
        Optional<User> maybeUser;

        String encryptedPassword = passwordEncoder.encode(newUserData.getPassword());
        User newUser = new User()
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
    public Optional<User> findById(Long userId) {
        if (userId == null) return Optional.empty();
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findAllByEmails(List<String> emails) {
        return userRepository.findByEmails(emails);
    }

    @Override
    public Optional<User> updateUser(User userData) {
        boolean isUserExist = userRepository.existsById(userData.getId());
        if (!isUserExist) return Optional.empty();
        User updatedUser = userRepository.save(userData);
        return Optional.ofNullable(updatedUser);
    }

    @Override
    public Boolean validatePassword(Long userId, String password) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Boolean.FALSE;
        }
        User registeredUser = maybeUser.get();
        return Boolean.valueOf(passwordEncoder.matches(password, registeredUser.getPassword()));
    }

    @Override
    public Boolean checkEmailIsAvailable(String email) {
        Optional<User> maybeDuplicate = userRepository.findByEmail(email);
        return Boolean.valueOf(maybeDuplicate.isEmpty());
    }

    @Override
    public Boolean checkEmailIsAvailable(String email, Long userId) {
        Optional<User> maybeDuplicate = userRepository.findByEmail(email);
        if (maybeDuplicate.isEmpty()) return Boolean.TRUE;
        User duplicate = maybeDuplicate.get();
        return duplicate.getId().equals(userId);
    }

    @Override
    public Boolean checkTagNameIsAvailable(String tagName) {
        Optional<User> maybeDuplicate = userRepository.findByTagName(tagName);
        return Boolean.valueOf(maybeDuplicate.isEmpty());
    }

    @Override
    public Boolean checkTagNameIsAvailable(String tagName, Long userId) {
        Optional<User> maybeDuplicate = userRepository.findByTagName(tagName);
        if (maybeDuplicate.isEmpty()) return Boolean.TRUE;
        User duplicate = maybeDuplicate.get();
        return duplicate.getId().equals(userId);
    }

    @Override
    public List<User> findMembersOfPage(Long pageId) {
        Page page = pageRepository.findById(pageId).orElseThrow(ResourceNotFoundException::new);
        while(page.getCollection() != null) {
            page = page.getCollection().getPage();
        }
        return userRepository.findAllByRootPageId(page.getId());
    }
}
