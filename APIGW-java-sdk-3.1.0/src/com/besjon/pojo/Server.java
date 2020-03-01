package com.besjon.pojo;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Auto-generated: 2020-02-07 12:18:56
 * @deprecation: 从gson类中接收类，类中属性就是json中节点中的属性，必须继承BaseCloud
 * id，name已经在定义在BaseCloud中
 * @author zhuhanbin
 */
@Data
public class Server extends BaseCloud{

    private Flavor flavor;
    @SerializedName("status")
    private String runStatus;
    /**
     * 云主机ID
     */
    @SerializedName("id")
    private String serverId;
    private Map<String, ArrayList<Address>> addresses;
    private  Image metadata;
    private String[]tags;
}