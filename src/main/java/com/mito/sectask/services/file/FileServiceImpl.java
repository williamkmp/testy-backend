package com.mito.sectask.services.file;

import com.mito.sectask.dto.dto.FilePreviewDto;
import com.mito.sectask.entities.File;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.repositories.FileRepository;
import jakarta.transaction.Transactional;
import java.util.Date;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private String GET_FILE_URL = "http://localhost:5000/file/";
    private final FileRepository fileRepository;

    @Override
    public Optional<File> findById(Long fileId) {
        try {
            if (fileId == null) throw new ResourceNotFoundException();
            File file = fileRepository
                .findById(fileId)
                .orElseThrow(ResourceNotFoundException::new);
            return Optional.of(file);
        } catch (Exception e) {
            if (fileId > 0) {
                log.error("File not found id:{}", fileId);
                e.printStackTrace();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<File> saveUploadFormdata(MultipartFile file) {
        try {
            File newFile = new File()
                .setName(file.getOriginalFilename())
                .setBytes(file.getBytes())
                .setContentType(file.getContentType())
                .setCreatedAt(new Date());
            newFile = fileRepository.saveAndFlush(newFile);
            return Optional.of(newFile);
        } catch (Exception e) {
            log.error("Error saving file");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getFileUrl(Long fileId) {
        try {
            if (fileId == null) throw new ResourceNotFoundException();
            File file = fileRepository
                .findById(fileId)
                .orElseThrow(ResourceNotFoundException::new);
            String clientAccessURL = constructClientAccessUrl(file.getId());
            return Optional.of(clientAccessURL);
        } catch (Exception e) {
            if (fileId > 0) {
                log.error(
                    "Error constructing client get file url id:{}",
                    fileId
                );
                e.printStackTrace();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<FilePreviewDto> createPreview(File file) {
        if (file == null) return Optional.empty();
        return Optional.of(
            new FilePreviewDto()
                .setId(file.getId().toString())
                .setName(file.getName())
                .setExtension(file.getContentType())
                .setSize(Long.valueOf(file.getBytes().length))
                .setUrl(constructClientAccessUrl(file.getId()))
        );
    }

    private String constructClientAccessUrl(Long fileId) {
        return (GET_FILE_URL + fileId.toString());
    }

    @Override
    @Transactional
    public Optional<File> deleteById(Long fileId) {
        try {
            File file = fileRepository
                .findById(fileId)
                .orElseThrow(ResourceNotFoundException::new);
            fileRepository.deleteById(file.getId());
            return Optional.of(file);
        } catch (Exception e) {
            if (fileId > 0) {
                log.error("Error deleting file id:{}", fileId);
                e.printStackTrace();
            }
            return Optional.empty();
        }
    }
}
