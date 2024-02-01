package com.mito.sectask.services.file;

import com.mito.sectask.dto.dto.FilePreviewDto;
import com.mito.sectask.entities.File;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.repositories.FileRepository;
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
            log.error("File not found id:{}", fileId);
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<FilePreviewDto> saveUploadFormdata(MultipartFile file) {
        try {
            File newFile = new File()
                .setName(file.getOriginalFilename())
                .setBytes(file.getBytes())
                .setContentType(file.getContentType())
                .setCreatedAt(new Date());
            newFile = fileRepository.saveAndFlush(newFile);
            String url = getFileUrl(newFile.getId())
                .orElseThrow(ResourceNotFoundException::new);
            return Optional.of(
                new FilePreviewDto()
                    .setId(newFile.getId().toString())
                    .setName(newFile.getName())
                    .setSize(file.getSize())
                    .setExtension(newFile.getContentType())
                    .setUrl(url)
            );
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
            return Optional.of(GET_FILE_URL + file.getId().toString());
        } catch (Exception e) {
            log.error("Error constructing file url id:{}", fileId);
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
