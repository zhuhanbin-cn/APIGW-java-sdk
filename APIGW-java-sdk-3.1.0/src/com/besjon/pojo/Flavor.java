package com.besjon.pojo;

import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2020-02-07 15:35:35
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class Flavor extends BaseCloud {
    private String name;
    private String id;
    private String vcpus;
    private Integer ram;
    private String disk;
    private String swap;
    private List<Link> links;
}