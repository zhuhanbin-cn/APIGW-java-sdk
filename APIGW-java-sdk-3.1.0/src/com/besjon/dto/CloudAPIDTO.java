package com.besjon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
* 传参类
* 根据需要删减无效参数
* @package:com.zjbdos.ebb.settingconfig.client.vo
* @author: zhuhanbin
* @create: 2020/01/23
**/
@Data
@NoArgsConstructor
@AllArgsConstructor

public class CloudAPIDTO {

    private String id;
    private Integer type;
    private String code;
    private String name;
    private String serviceCode;
    private String version;
    private String projectId;
    private String serverId;
    private String regionCode;
    private String platformCode;
    private String platformName;
    private String scheme;
    private String requestMethod;
    private String endpoint;
    private String resourcePath;

}