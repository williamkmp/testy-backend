package com.mito.sectask.services.image.impl;

import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Service;
import com.mito.sectask.entities.Image;
import com.mito.sectask.repositories.ImageRepository;
import com.mito.sectask.services.image.ImageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

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
}
