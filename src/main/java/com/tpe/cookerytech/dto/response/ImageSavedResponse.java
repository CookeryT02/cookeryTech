package com.tpe.cookerytech.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageSavedResponse extends CkResponse{
    private String imageId;

    public ImageSavedResponse(String imageId, String message, boolean success){
        super(message,success);
        this.imageId=imageId;
    }
}
