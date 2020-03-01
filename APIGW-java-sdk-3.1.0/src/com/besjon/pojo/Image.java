package com.besjon.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Image {
    @SerializedName("metering.image_id")
    private String imageId;
    @SerializedName("metering.imagetype")
    private String imageType;
    private String image_name;
    private String os_bit;
    private String os_type;
}