package com.besjon.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Address {
    private String version;
    private String addr;
    @SerializedName("OS-EXT-IPS-MAC:mac_addr")
    private String mac_addr;
    @SerializedName("OS-EXT-IPS:type")
    private String type;
}