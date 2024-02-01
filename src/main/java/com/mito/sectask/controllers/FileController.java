package com.mito.sectask.controllers;

import com.mito.sectask.annotations.Authenticated;
import com.mito.sectask.dto.dto.FilePreviewDto;
import com.mito.sectask.dto.response.Response;
import com.mito.sectask.services.image.ImageService;
import com.mito.sectask.values.MESSAGES;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/file")
@AllArgsConstructor
public class FileController {

    private ImageService imageService;

    @GetMapping("/{fileId}/preview")
    public Response<FilePreviewDto> get(@PathVariable("fileId") Long id) {
        // TODO: impelemet ge file preview
        return new Response<FilePreviewDto>(HttpStatus.OK)
            .setData(
                new FilePreviewDto()
                    .setId(null)
                    .setName(null)
                    .setSize(null)
                    .setUrl(null)
                    .setExtension(null)
            );
    }

    @PostMapping(
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Authenticated(true)
    public Response<FilePreviewDto> uploadImage(
        @RequestParam("file") MultipartFile file
    ) {
        //TODO: implement file upload
        try {
            return new Response<FilePreviewDto>(HttpStatus.CREATED)
                .setMessage(MESSAGES.UPLOAD_SUCCESS)
                .setData(
                    new FilePreviewDto()
                        .setId(null)
                        .setName(null)
                        .setSize(null)
                        .setUrl(null)
                        .setExtension(null)
                );
        } catch (Exception e) {
            return new Response<FilePreviewDto>(HttpStatus.BAD_REQUEST)
                .setMessage(MESSAGES.UPLOAD_FAIL);
        }
    }
}
