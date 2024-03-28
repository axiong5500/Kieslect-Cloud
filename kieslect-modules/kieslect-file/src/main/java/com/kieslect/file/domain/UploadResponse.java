package com.kieslect.file.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UploadResponse {
    private String filename;
    private String fileUrl;
    private Long fileSize;
}
