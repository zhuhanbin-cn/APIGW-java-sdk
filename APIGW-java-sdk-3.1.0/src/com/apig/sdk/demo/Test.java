package com.apig.sdk.demo;

import com.besjon.pojo.JsonRootBean;
import com.google.gson.Gson;

public class Test {
    public static void main(String[] args){
        String s1="{\n" +
                " \"servers\": [\n" +
                "  {\n" +
                "   \"tenant_id\": \"06dd8c78db8026442f7bc00dda0ddb72\",\n" +
                "   \"metadata\": {\n" +
                "    \"__support_agent_list\": \"hss\"\n" +
                "   },\n" +
                "   \"addresses\": {\n" +
                "    \"91bd0206-02cd-4056-afd4-2bc2714c6b4b\": [\n" +
                "     {\n" +
                "      \"OS-EXT-IPS-MAC:mac_addr\": \"fa:16:3e:ef:ba:b0\",\n" +
                "      \"addr\": \"192.168.1.155\",\n" +
                "      \"OS-EXT-IPS:type\": \"fixed\",\n" +
                "      \"version\": 4\n" +
                "     },\n" +
                "     {\n" +
                "      \"OS-EXT-IPS-MAC:mac_addr\": \"fa:16:3e:ef:ba:b0\",\n" +
                "      \"addr\": \"119.3.154.102\",\n" +
                "      \"OS-EXT-IPS:type\": \"floating\",\n" +
                "      \"version\": 4\n" +
                "     }\n" +
                "    ]\n" +
                "   },\n" +
                "   \"OS-EXT-STS:task_state\": null,\n" +
                "   \"OS-DCF:diskConfig\": \"MANUAL\",\n" +
                "   \"OS-EXT-AZ:availability_zone\": \"cn-east-3a\",\n" +
                "   \"links\": [\n" +
                "    {\n" +
                "     \"rel\": \"self\",\n" +
                "     \"href\": \"https://ecs-api.cn-east-3.myhuaweicloud.com/v2.1/06dd8c78db8026442f7bc00dda0ddb72/servers/b5c4dce6-9573-4664-a1be-b57075365bcd\"\n" +
                "    },\n" +
                "    {\n" +
                "     \"rel\": \"bookmark\",\n" +
                "     \"href\": \"https://ecs-api.cn-east-3.myhuaweicloud.com/06dd8c78db8026442f7bc00dda0ddb72/servers/b5c4dce6-9573-4664-a1be-b57075365bcd\"\n" +
                "    }\n" +
                "   ],\n" +
                "   \"OS-EXT-STS:power_state\": 1,\n" +
                "   \"id\": \"b5c4dce6-9573-4664-a1be-b57075365bcd\",\n" +
                "   \"os-extended-volumes:volumes_attached\": [\n" +
                "    {\n" +
                "     \"id\": \"8a6739bf-dbe5-46ba-b596-8cca1555ec9f\"\n" +
                "    }\n" +
                "   ],\n" +
                "   \"OS-EXT-SRV-ATTR:host\": \"pod03.cn-east-3a\",\n" +
                "   \"accessIPv4\": \"\",\n" +
                "   \"image\": {\n" +
                "    \"links\": [\n" +
                "     {\n" +
                "      \"rel\": \"bookmark\",\n" +
                "      \"href\": \"https://ecs-api.cn-east-3.myhuaweicloud.com/06dd8c78db8026442f7bc00dda0ddb72/images/5179ab33-cbbe-4e55-b2bf-abeb4f32959c\"\n" +
                "     }\n" +
                "    ],\n" +
                "    \"id\": \"5179ab33-cbbe-4e55-b2bf-abeb4f32959c\"\n" +
                "   },\n" +
                "   \"OS-SRV-USG:terminated_at\": null,\n" +
                "   \"accessIPv6\": \"\",\n" +
                "   \"created\": \"2020-02-16T10:40:04Z\",\n" +
                "   \"hostId\": \"c38cc0ff9d51b416c0a8f075bc452aeb6d6884a826d3cef2d029183b\",\n" +
                "   \"OS-EXT-SRV-ATTR:hypervisor_hostname\": \"nova002@37\",\n" +
                "   \"flavor\": {\n" +
                "    \"links\": [\n" +
                "     {\n" +
                "      \"rel\": \"bookmark\",\n" +
                "      \"href\": \"https://ecs-api.cn-east-3.myhuaweicloud.com/06dd8c78db8026442f7bc00dda0ddb72/flavors/c6.large.2\"\n" +
                "     }\n" +
                "    ],\n" +
                "    \"id\": \"c6.large.2\"\n" +
                "   },\n" +
                "   \"key_name\": null,\n" +
                "   \"security_groups\": [\n" +
                "    {\n" +
                "     \"name\": \"sg-fead\"\n" +
                "    }\n" +
                "   ],\n" +
                "   \"config_drive\": \"\",\n" +
                "   \"OS-EXT-STS:vm_state\": \"active\",\n" +
                "   \"user_id\": \"06dd5f910200105e1f22c00df98d67df\",\n" +
                "   \"OS-EXT-SRV-ATTR:instance_name\": \"instance-00087591\",\n" +
                "   \"name\": \"ecs-9f73\",\n" +
                "   \"progress\": 0,\n" +
                "   \"OS-SRV-USG:launched_at\": \"2020-02-16T10:40:19.000000\",\n" +
                "   \"updated\": \"2020-02-16T10:40:55Z\",\n" +
                "   \"status\": \"ACTIVE\"\n" +
                "  }\n" +
                " ]\n" +
                "}";

        JsonRootBean jsonRootBean11= new Gson().fromJson(s1, JsonRootBean.class );
        jsonRootBean11.getServers().forEach(server -> {
            System.out.println(server);
        });
    }
}