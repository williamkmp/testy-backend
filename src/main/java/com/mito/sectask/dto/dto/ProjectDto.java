package com.mito.sectask.dto.dto;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ProjectDto {

    private String name;
    private String description;
    private Date startDate;
    private Date endDate;
    private Date createdAt;
    private UserDto owner;
}
