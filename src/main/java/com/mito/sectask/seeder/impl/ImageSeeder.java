package com.mito.sectask.seeder.impl;

import com.mito.sectask.entities.File;
import com.mito.sectask.repositories.FileRepository;
import com.mito.sectask.seeder.Seeder;
import com.mito.sectask.utils.Util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ImageSeeder implements Seeder {

    private final FileRepository fileRepository;

    @Override
    public void seed() throws Exception {
        String[] imageNames = new String[] {"cover1.jpeg", "cover2.jpeg", "cover3.jpeg", "block1.jpg"};

        List<File> images = constructImages(imageNames);
        fileRepository.saveAllAndFlush(images);
    }

    private List<File> constructImages(String[] imageNames) {
        List<File> files = new ArrayList<>();
        final String projectDirectoryPath = new FileSystemResource("").getFile().getAbsolutePath();
        final String assetDirectoryPath = Paths.get(
                        projectDirectoryPath, "src", "main", "java", "com", "mito", "sectask", "seeder", "assets")
                .toString();
        for (String imageName : imageNames) {
            Path imagePath = Paths.get(assetDirectoryPath, imageName);
            try {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                String contentType = "image/" + getFileExtension(imageName);
                files.add(new File()
                        .setBytes(imageBytes)
                        .setContentType(contentType)
                        .setCreatedAt(new Date()));
            } catch (IOException e) {
                e.printStackTrace();
                Util.doNothing("continue loop don't create file");
            }
        }
        return files;
    }

    private String getFileExtension(String fileName) {
        String[] partition = fileName.split("\\.");
        return partition[partition.length - 1];
    }
}
