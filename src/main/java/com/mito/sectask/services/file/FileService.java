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
     * @return {@link Optional}<{@link File}> saved file information, else empty if operation failed
     */
    public Optional<File> saveUploadFormdata(MultipartFile file);

    /**
     * get the static url for a given file id
     *
     * @param fileId {@link Long} file id
     * @return {@link Optional}<{@link Stirng}> file url, empty if operation failed
     */
    public Optional<String> getFileUrl(Long fileId);

    /**
     * find file entity by id
     *
     * @param fileId {@link Long} file id
     * @return {@link Optional}<{@link File}> file entity, else empty if operation failed
     */
    public Optional<File> findById(Long fileId);

    /**
     * create a preview from a given file
     *
     * @param file {@link File} file entity, not null
     * @return {@link Optional}<{@link FilePreviewDto}> file preview, else empty if given file null or incomplete data
     */
    public Optional<FilePreviewDto> createPreview(File file);

    /**
     * delete a file in teh database by a given id
     *
     * @param fileId {@link Long} file id
     * @return {@link Optional}<{@link File}> delted file entity, else empty if operation failed
     */
    public Optional<File> deleteById(Long fileId);
}
