package com.mito.sectask.dto.request.project;

import com.mito.sectask.values.VALIDATION;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.Value;

@Value
public class ProjectCreateRequest {

    @NotBlank(message = VALIDATION.REQUIRED)
    @Size(
        min = 1,
        max = 100,
        message = VALIDATION.STRING_LENGTH + 1 + "," + 100
    )
    private String projectName;

    private String projectDescription;

    @NotNull(message = VALIDATION.REQUIRED)
    private Date projectStartDate;

    @NotNull(message = VALIDATION.REQUIRED)
    private Date projectEndDate;
}
