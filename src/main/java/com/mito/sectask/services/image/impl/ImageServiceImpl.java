package com.mito.sectask.services.image.impl;

import com.mito.sectask.entities.ImageEntity;
import com.mito.sectask.repositories.ImageRepository;
import com.mito.sectask.services.image.ImageService;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Override
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
    public Optional<ImageEntity> findById(Long id) {
        return imageRepository.findById(id);
    }
}
