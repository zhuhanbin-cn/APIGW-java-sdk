package com.besjon.pojo;

import com.annotations.BaseCloudAnnotation;
import lombok.Data;

@Data
public class BaseCloud {
    @BaseCloudAnnotation(isRetainedField = true)
    private String platformCode;
    @BaseCloudAnnotation(isRetainedField = true)
    private String regionCode;

}