package com.mito.sectask.dto.response.project;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class CreateProjectResponse {

    @NonNull
    private String projectId;

    @NonNull
    private String projectName;

    @NonNull
    private String projectDescription;

    @NonNull
    private Date projectStartDate;

    @NonNull
    private Date projectEndDate;
}
