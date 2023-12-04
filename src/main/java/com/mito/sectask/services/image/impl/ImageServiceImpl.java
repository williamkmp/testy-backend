package com.mito.sectask.services.image.impl;

import com.mito.sectask.entities.Image;
import com.mito.sectask.entities.Project;
import com.mito.sectask.entities.User;
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
    public Optional<Image> findById(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Image> saveImage(byte[] imageBinary) {
        Image newImage = new Image()
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
    public Optional<Image> saveUserImage(
        Long userId,
        byte[] imageBinary
    ) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Optional.empty();
        }

        User user = maybeUser.get();
        Image userProfilePicture = user.getImage();
        if (userProfilePicture != null) {
            imageRepository.delete(userProfilePicture);
        }

        Optional<Image> maybeImage = saveImage(imageBinary);
        if (maybeImage.isEmpty()) {
            return Optional.empty();
        }
        Image newProfilePicture = maybeImage.get();
        user.setImage(newProfilePicture);
        userRepository.save(user);
        return Optional.of(newProfilePicture);
    }

    @Override
    @Transactional
    public Optional<Image> findUserProfilePicture(Long userId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Optional.empty();
        }
        Image userProfilePicture = maybeUser.get().getImage();
        if (userProfilePicture != null) userProfilePicture.getFile();
        return Optional.ofNullable(userProfilePicture);
    }

    @Override
    @Transactional
    public Optional<Image> deleteUserProfilePicture(Long userId) {
        Optional<User> maybeUser = userRepository.findById(userId);
        if (maybeUser.isEmpty()) {
            return Optional.empty();
        }

        User user = maybeUser.get();
        Image userProfilePicture = user.getImage();
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
    public Optional<Image> saveProjectImage(
        Long projectId,
        byte[] imageBinary
    ) {
        Optional<Project> maybeProject = projectRepository.findById(
            projectId
        );
        if (maybeProject.isEmpty()) {
            return Optional.empty();
        }

        Project project = maybeProject.get();
        Image projectPicture = project.getProfileImage();
        if (projectPicture != null) {
            imageRepository.delete(projectPicture);
        }

        Optional<Image> maybeImage = saveImage(imageBinary);
        if (maybeImage.isEmpty()) {
            return Optional.empty();
        }
        Image newPicture = maybeImage.get();
        project.setProfileImage(newPicture);
        projectRepository.save(project);
        return Optional.of(newPicture);
    }

    @Override
    public Optional<Image> getProjectPicture(Long projectId) {
        Optional<Project> maybeProject = projectRepository.findById(
            projectId
        );
        if (maybeProject.isEmpty()) {
            return Optional.empty();
        }
        Image picture = maybeProject.get().getProfileImage();
        if (picture != null) picture.getFile();
        return Optional.ofNullable(picture);
    }

    @Override
    public Optional<Image> deleteProjectPicture(Long projectId) {
        Optional<Project> maybeProject = projectRepository.findById(
            projectId
        );
        if (maybeProject.isEmpty()) {
            return Optional.empty();
        }

        Project project = maybeProject.get();
        Image picture = project.getProfileImage();
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
