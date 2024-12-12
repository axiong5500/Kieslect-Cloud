package com.kieslect.file.domain.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class StravaFileUploadsVO {
    private String file;
    private String name;
    private String description;
    @JsonProperty("data_type")
    private String dataType;

    public Map<String, Object> toMap() {
        return Map.of(
                "file", file,
                "name", name,
                "description", description,
                "data_type", dataType
        );
    }
}
