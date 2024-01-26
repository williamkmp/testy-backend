package com.mito.sectask.services.image;

import com.mito.sectask.entities.File;
import com.mito.sectask.entities.Page;
import com.mito.sectask.entities.User;
import com.mito.sectask.repositories.FileRepository;
import com.mito.sectask.repositories.PageRepository;
import com.mito.sectask.repositories.UserRepository;

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
    private final PageRepository pageRepository;

    @Override
    public Optional<File> findById(Long id) {
        if (id == null) return Optional.empty();
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
        if (imageId == null) {
            return Optional.empty();
        }
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
        if (maybeUser.isEmpty()) return Optional.empty();
        User user = maybeUser.get();

        // Delete previous image
        File previousImage = user.getImage();
        if (previousImage != null && !previousImage.getId().equals(imageId)) {
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

    @Override
    @Transactional
    public Optional<File> updatePageCoverImage(Long pageId, Long imageId) {
        Optional<Page> page = pageRepository.findById(pageId);
        if (page.isEmpty()) {
            return Optional.empty();
        }

        // Delete previous image
        File previousImage = page.get().getImage();
        if (previousImage != null && !previousImage.getId().equals(imageId)) {
            fileRepository.delete(previousImage);
        }

        // Updating page image relation
        if (imageId == null) {
            page.get().setImage(null);
        } else {
            File newImage = fileRepository.findById(imageId).orElse(null);
            page.get().setImage(newImage);
        }
        pageRepository.save(page.get());
        return Optional.ofNullable(page.get().getImage());
    }
}
