package com.mito.sectask.services.image.impl;

import com.mito.sectask.entities.ImageEntity;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.repositories.ImageRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.image.ImageService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<ImageEntity> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<ImageEntity> saveImage(byte[] imageBinary) {
        ImageEntity newImage = new ImageEntity()
            .setFile(imageBinary)
            .setCreatedAt(new Date());

        try {
            newImage = imageRepository.save(newImage);
        } catch (Exception e) {
            log.error("Save image failed", e);
            return Optional.empty();
        }

        return Optional.of(newImage);
    }

    @Override
    @Transactional
    public Optional<ImageEntity> saveUserImage(
        Long userId,
        byte[] imageBinary
    ) {
        Optional<UserEntity> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Optional.empty();
        }

        UserEntity user = maybeUser.get();
        ImageEntity userProfilePicture = user.getImage();
        if (userProfilePicture != null) {
            imageRepository.delete(userProfilePicture);
        }

        Optional<ImageEntity> maybeImage = saveImage(imageBinary);
        if (maybeImage.isEmpty()) {
            return Optional.empty();
        }
        ImageEntity newProfilePicture = maybeImage.get();
        user.setImage(newProfilePicture);
        userRepository.save(user);
        return Optional.of(newProfilePicture);
    }

    @Override
    @Transactional
    public Optional<ImageEntity> findUserProfilePicture(Long userId) {
        Optional<UserEntity> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Optional.empty();
        }
        ImageEntity userProfilePicture = maybeUser.get().getImage();
        if (userProfilePicture != null) userProfilePicture.getFile();
        return Optional.ofNullable(userProfilePicture);
    }

    @Override
    @Transactional
    public Optional<ImageEntity> deleteUserProfilePicture(Long userId) {
        Optional<UserEntity> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Optional.empty();
        }

        UserEntity user = maybeUser.get();
        ImageEntity userProfilePicture = user.getImage();
        if (userProfilePicture != null) {
            imageRepository.delete(userProfilePicture);
            return Optional.ofNullable(userProfilePicture);
        }

        return Optional.empty();
    }
}
