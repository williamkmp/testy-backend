package com.mito.sectask.dto.request.page;

import com.mito.sectask.dto.dto.InviteDto;
import java.util.List;
import lombok.Value;

@Value
public class PageCreateRequest {

    private String collectionId;

    private String title;

    private List<InviteDto> members;

    private String iconKey;

    private String imageId;

    private Float imagePosition;
}
