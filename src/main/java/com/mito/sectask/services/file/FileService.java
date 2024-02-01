package com.mito.sectask.services.file;

import com.mito.sectask.dto.dto.FilePreviewDto;
import com.mito.sectask.entities.File;
import java.util.Optional;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * save a given MultipartFormdata file request to the database
     *
     * @param file {@link MultipartFile} formdata request
     * @return {@link Optional}<{@link FilePreviewDto}> saved file information, else empty if operation failed
     */
    public Optional<FilePreviewDto> saveUploadFormdata(MultipartFile file);

    /**
     * get the static url for a given file id
     *
     * @param fileId {@link Long} file id
     * @return {@link Optional}<{@link Stirng}> file url, empty if operation failed
     */
    public Optional<String> getFileUrl(Long fileId);

    /**
     * find file entity by id
     * @param fileId {@link Long} file id
     * @return {@link Optional}<{@link File}> file entity, else empty if operation failed
     */
    public Optional<File> findById(Long fileId);
}
