package com.mito.sectask.services.image.impl;

import com.mito.sectask.entities.File;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.FileRepository;
import com.mito.sectask.repositories.UserRepository;
import com.mito.sectask.services.image.ImageService;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    @Override
    public Optional<File> findById(Long id) {
        return fileRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<File> saveImage(byte[] imageBinary, String extension) {
        File newImage = new File()
            .setBytes(imageBinary)
            .setContentType("image/" + extension)
            .setCreatedAt(new Date());

        try {
            newImage = fileRepository.save(newImage);
        } catch (Exception e) {
            log.error("Save image failed", e);
            return Optional.empty();
        }

        return Optional.of(newImage);
    }

    @Override
    public Optional<String> getImageUrl(Long imageId) {
        final String GET_IMAGE_URL = "http://localhost:5000/image/";
        Optional<File> maybeFile = fileRepository.findById(imageId);
        if (maybeFile.isEmpty()) {
            return Optional.empty();
        }
        File file = maybeFile.get();
        if (!file.getContentType().startsWith("image/")) {
            return Optional.empty();
        }
        return Optional.of(GET_IMAGE_URL + file.getId().toString());
    }

    @Override
    @Transactional
    public Optional<File> updateUserImage(Long userId, Long imageId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if(maybeUser.isEmpty()) return Optional.empty();
        User user = maybeUser.get();

        // Delete previous image 
        File previousImage = user.getImage();
        if (previousImage != null) {
            fileRepository.delete(previousImage);
        } 

        // Delete image
        if (imageId == null) {
            user.setImage(null);
        }
        // Update to new image
        else {
            File newImage = fileRepository.findById(imageId).orElse(null);
            user.setImage(newImage);
        }

        userRepository.save(user); 
        return Optional.ofNullable(user.getImage());
    }
}
