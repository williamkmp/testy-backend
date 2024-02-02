package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.dto.FilePreviewDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.entities.File;
import com.mito.sectask.exceptions.exceptions.MismatchedDataException;
import com.mito.sectask.exceptions.exceptions.ResourceNotFoundException;
import com.mito.sectask.exceptions.httpexceptions.InternalServerErrorHttpException;
import com.mito.sectask.exceptions.httpexceptions.ResourceNotFoundHttpException;
import com.mito.sectask.services.file.FileService;
import com.mito.sectask.values.MESSAGES;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping(path = "/file")
@AllArgsConstructor
public class FileController {

    private FileService fileService;

    @GetMapping("/{fileId}")
    public ResponseEntity<byte[]> downloadFile(
        @PathVariable("fileId") Long fileId
    ) {
        try {
            File requestedFile = fileService
                .findById(fileId)
                .orElseThrow(ResourceNotFoundException::new);
            return ResponseEntity
                .ok()
                .header("Content-Type", requestedFile.getContentType())
                .body(requestedFile.getBytes());
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundHttpException();
        } catch (Exception e) {
            log.error("Error requesting file id:{}", fileId);
            e.printStackTrace();
            throw new InternalServerErrorHttpException();
        }
    }

    @GetMapping("/{fileId}/preview")
    @Authenticated(true)
    public Response<FilePreviewDto> getFilePreview(
        @PathVariable("fileId") Long fileId
    ) {
        try {
            File file = fileService
                .findById(fileId)
                .orElseThrow(ResourceNotFoundHttpException::new);
            FilePreviewDto filePreview = fileService
                .createPreview(file)
                .orElseThrow(ResourceNotFoundHttpException::new);
            return new Response<FilePreviewDto>(HttpStatus.OK)
                .setData(filePreview);
        } catch (ResourceNotFoundHttpException e) {
            throw new ResourceNotFoundHttpException();
        } catch (Exception e) {
            log.error("Error getting file preview request.fileId: {}", fileId);
            e.printStackTrace();
            throw new InternalServerErrorHttpException();
        }
    }

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public Response<FilePreviewDto> uploadFile(
        @RequestParam("file") MultipartFile formData
    ) {
        try {
            File file = fileService
                .saveUploadFormdata(formData)
                .orElseThrow(MismatchedDataException::new);
            FilePreviewDto filePreview = fileService
                .createPreview(file)
                .orElseThrow(MismatchedDataException::new);
            return new Response<FilePreviewDto>(HttpStatus.CREATED)
                .setMessage(MESSAGES.UPLOAD_SUCCESS)
                .setData(filePreview);
        } catch (Exception e) {
            return new Response<FilePreviewDto>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.UPLOAD_FAIL);
        }
    }
}
