package com.mito.sectask.services.image.impl;

import com.mito.sectask.entities.ImageEntity;
import com.mito.sectask.entities.ProjectEntity;
import com.mito.sectask.entities.UserEntity;
import com.mito.sectask.repositories.ImageRepository;
import com.mito.sectask.repositories.ProjectRepository;
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
    private final ProjectRepository projectRepository;

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
        user.setImage(null);
        userRepository.save(user);

        if (userProfilePicture != null) {
            Long imageId = userProfilePicture.getId();
            imageRepository.deleteById(imageId);
            return Optional.ofNullable(userProfilePicture);
        }

        return Optional.empty();
    }

    @Override
    public Optional<ImageEntity> saveProjectImage(
        Long projectId,
        byte[] imageBinary
    ) {
        Optional<ProjectEntity> maybeProject = projectRepository.findById(
            projectId
        );
        if (maybeProject.isEmpty()) {
            return Optional.empty();
        }

        ProjectEntity project = maybeProject.get();
        ImageEntity projectPicture = project.getProfileImage();
        if (projectPicture != null) {
            imageRepository.delete(projectPicture);
        }

        Optional<ImageEntity> maybeImage = saveImage(imageBinary);
        if (maybeImage.isEmpty()) {
            return Optional.empty();
        }
        ImageEntity newPicture = maybeImage.get();
        project.setProfileImage(newPicture);
        projectRepository.save(project);
        return Optional.of(newPicture);
    }

    @Override
    public Optional<ImageEntity> getProjectPicture(Long projectId) {
        Optional<ProjectEntity> maybeProject = projectRepository.findById(
            projectId
        );
        if (maybeProject.isEmpty()) {
            return Optional.empty();
        }
        ImageEntity picture = maybeProject.get().getProfileImage();
        if (picture != null) picture.getFile();
        return Optional.ofNullable(picture);
    }

    @Override
    public Optional<ImageEntity> deleteProjectPicture(Long projectId) {
        Optional<ProjectEntity> maybeProject = projectRepository.findById(
            projectId
        );
        if (maybeProject.isEmpty()) {
            return Optional.empty();
        }

        ProjectEntity project = maybeProject.get();
        ImageEntity picture = project.getProfileImage();
        project.setProfileImage(null);
        projectRepository.save(project);

        if (picture != null) {
            Long imageId = picture.getId();
            imageRepository.deleteById(imageId);
            return Optional.ofNullable(picture);
        }

        return Optional.empty();
    }
}
