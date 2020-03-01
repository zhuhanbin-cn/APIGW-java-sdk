//package name of the demo.
package com.apig.sdk.demo;

//Import the external dependencies.

import com.annotations.BaseCloudAnnotation;
import com.besjon.dto.CloudAPIDTO;
import com.besjon.pojo.JsonRootBean;
import com.cloud.apigateway.sdk.utils.Client;
import com.cloud.apigateway.sdk.utils.Request;
import com.google.gson.*;
import lombok.Data;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Main {
    private static final Pattern pattern = Pattern.compile("\\{(.*?)\\}");
    private static Matcher matcher;
    public static String getFieldValueByFieldName(String fieldName,Object object) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            //对private的属性的访问
            field.setAccessible(true);
            return (String) field.get(object);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     * 格式化字符串 字符串中使用{key}表示占位符 利用反射 自动获取对象属性值 (必须有get方法)
     *
     * @param sourStr
     *            需要匹配的字符串
     * @param obj
     *            参数集
     * @return
     */
    public static String stringFormat(String sourStr, Object obj) {
        String tagerStr = sourStr;
        matcher = pattern.matcher(tagerStr);
        if (obj == null)
            return tagerStr;

        PropertyDescriptor pd;
        Method getMethod;
        // 匹配{}中间的内容 包括括号
        while (matcher.find()) {
            String key = matcher.group();
            String keyclone = key.substring(1, key.length() - 1).trim();
            try {
                pd = new PropertyDescriptor(keyclone, obj.getClass());
                getMethod = pd.getReadMethod();// 获得get方法
                Object value = getMethod.invoke(obj);
                if (value != null)
                    tagerStr = tagerStr.replace(key, value.toString());
            } catch (Exception e) {
                // TODO Auto-generated catch block
                // Loggers.addException(e);
            }
        }
        System.out.println(tagerStr);
        return tagerStr;
    }

    /**
     * 格式化字符串 字符串中使用{key}表示占位符 利用反射 自动获取对象属性值 (必须有get方法)
     *
     * @param sourStr
     *            需要匹配的字符串
     * @param obj
     *            参数集
     * @return
     */
    public static String paramStringFormat(String sourStr, Object obj) {
        if (sourStr == null)
            return sourStr;
        String tagerStr = sourStr;
        matcher = pattern.matcher(tagerStr);
        if (obj == null)
            return tagerStr;

        PropertyDescriptor pd;
        Method getMethod;
        // 匹配{}中间的内容 包括括号
        while (matcher.find()) {
            String key = matcher.group();
            String keyclone = key.substring(1, key.length() - 1).trim();
            try {
                pd = new PropertyDescriptor(keyclone, obj.getClass());
                getMethod = pd.getReadMethod();// 获得get方法
                Object value = getMethod.invoke(obj);
                if (value != null && value!=""){
                    tagerStr = tagerStr.replace(key, "&"+keyclone+"="+value.toString());
                }else{
                    tagerStr = tagerStr.replace(key, "");
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                // Loggers.addException(e);
            }
        }
        System.out.println(tagerStr);
        return tagerStr;
    }


    public static Request getRequest(CloudAPIDTO cloudAPIDTO){
        Request request = new Request();
        try {
            //Set the AK/SK to sign and authenticate the request.
            //华为
            //request.setKey("N3OPW1T2L0MK5HEHPZH5");
            //request.setSecret("iDYQAdv3i98Sgq4vtbGO9VtnLFAMBuCxzNardy57");
            //义数
            request.setKey("PZH7U3G1XSUBIIWQLDWB");
            request.setSecret("kzwIEH5anaNtoGdyidxQLX75hMaxgEMfedITjqy7");
            //Specify a request method, such as GET, PUT, POST, DELETE, HEAD, and PATCH.
            request.setMethod(cloudAPIDTO.getRequestMethod());
            //Set a request URL in the format of https://{Endpoint}/{URI}.
            request.setUrl((stringFormat(cloudAPIDTO.getResourcePath(),cloudAPIDTO)));
            //Set parameters for the request URL.
            //request.addQueryStringParam("limit", "2");

            //Add header parameters, for example, X-Domain-Id for invoking a global service and X-Project-Id for invoking a project-level service.
            request.addHeader("Content-Type", "application/json");
            request.addHeader("X-Project-Id", cloudAPIDTO.getProjectId());


            //request.addHeader("X-Auth-Token", "MIIa5QYJKoZIhvcNAQcCoIIa1jCCGtICAQExDTALBglghkgBZQMEAgEwghj3BgkqhkiG9w0BBwGgghjoBIIY5HsidG9rZW4iOnsiZXhwaXJlc19hdCI6IjIwMjAtMDItMTBUMTI6NDI6MDcuNzU3MDAwWiIsIm1ldGhvZHMiOlsicGFzc3dvcmQiXSwiY2F0YWxvZyI6W10sInJvbGVzIjpbeyJuYW1lIjoidGVfYWRtaW4iLCJpZCI6IjAifSx7Im5hbWUiOiJ0ZV9hZ2VuY3kiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9laXBfaXB2NiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Jkc19tY3MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF92MngiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3Nfc3BvdF9pbnN0YW5jZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2l2YXNfdmNyX3ZjYSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llZl9ub2RlZ3JvdXAiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jY2lfbW91bnRvYnNwb3NpeCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19hc2NlbmRfa2FpMSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Nlc19hZ3QiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kYnNfcmkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ibXNfaHBjX2gybGFyZ2UiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ldnNfZXNzZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2lvZHBzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYmF0Y2hfZWNzX2NsdXN0ZXIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3NfZ3B1X3YxMDAiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jYnNfcWkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kd3NfcG9jIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZXJzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbWVldGluZ19lbmRwb2ludF9idXkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3Nfb2ZmbGluZV9pcjMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9tZWVldGluZ193aGl0ZWJvYXJkX2J1eSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Zjc19CaW90ZWNoIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzcXVpY2tkZXBsb3kiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9WSVNfSW50bCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19ncHVfcDJzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZXZzX3ZvbHVtZV9yZWN5Y2xlX2JpbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3ZjYyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2RwcCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX29jc21hcnRjYW1wdXMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ia3MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3NfcmVjeWNsZWJpbiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX21lZXRpbmdfaGFyZGFjY291bnRfYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbXVsdGlfYmluZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX25scF9tdCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2VpcF9wb29sIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbWVlZXRpbmdfY3VycmVudF9idXkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pZWZfZnVuY3Rpb24iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hX2FwLXNvdXRoZWFzdC0zZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2ZpbmVfZ3JhaW5lZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Byb2plY3RfZGVsIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbTZtdCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2V2c19yZXR5cGUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hYWRfZnJlZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Jkc19wZzk0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWxiX2d1YXJhbnRlZWQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hX2NuLXNvdXRod2VzdC0yYiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Nmc3R1cmJvIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaHZfdmVuZG9yIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfbXJzX2FybV9yYzMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3NfaGkzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9jbi1ub3J0aC00ZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FfY24tbm9ydGgtNGQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9yZHNpMyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX0lFQyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3RhcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX25scF9sZ190ZyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3NlcnZpY2VzdGFnZV9tZ3JfZHRtIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9jbi1ub3J0aC00ZiIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NwaCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX01vZGVsQXJ0c0FzY2VuZDkxMCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX21lZXRpbmdfaGlzdG9yeV9jdXN0b21fYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGJzc19mcmVldHJhaWwiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF93cyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3Nkd2FuIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX2dwdV9nNXIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9ub3NxbF9jcmVhdGVSZWRpcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2VsYl9taWdyYXRlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfaW90ZWRnZV9jYW1wdXMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lbGJfbG9nX29hbSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3ZndmFzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdnBjX2Zsb3dfbG9nIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfb3BfZ2F0ZWRfaWNzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYWFkX2JldGFfaWRjIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY3Nic19yZXBfYWNjZWxlcmF0aW9uIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX3JjM19yczMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pZWZfZWRnZW1lc2giLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9haXNfYXBpX2ltYWdlX2FudGlfYWQiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kc3NfbW9udGgiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3NfYzZzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY3NnIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX2M2eCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3VmcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2RlY19tb250aF91c2VyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdmlwX2JhbmR3aWR0aCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19vbGRfcmVvdXJjZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19raTEiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9kY3NfcmkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jY2VfYnVyc3RfdG9fY2NpIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdmdpdnMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9vYnNfZHVhbHN0YWNrIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWRjbSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzYnNfcmVzdG9yZSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NycyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2l2c2NzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfcGNfdmVuZG9yX3N1YnVzZXIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pcHY2X2R1YWxzdGFjayIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2dhdGVkX2Vjc19yZWN5Y2xlYmluIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGxpX2FybV9wb2MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pcnRjIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfcGNhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfdmd3cyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2NzYnNfcHJvZ3Jlc3NiYXIiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pb3YtdHJpYWwiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9yZHNfYXJtIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfVGF1cnVzREJfYmV0YSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2V2c19wb29sX2NhIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGRzX2FybSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FfQ04tU09VVEgtMyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2RjczFfY3JyIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX29mZmxpbmVfZGlza180IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYnMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9nc3NfZnJlZV90cmlhbCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX21lZXRpbmdfY2xvdWRfYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY2Jzc3AiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lcHMiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9jc2JzX3Jlc3RvcmVfYWxsIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfMTIzIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfV2VMaW5rX2VuZHBvaW50X2J1eSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3F1aWNrYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY3B0c19jaGFvcyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Zjc19wYXkiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pb3RhbmFseXRpY3MiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9tYXhodWJfZW5kcG9pbnRfYnV5IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfYV9hcC1zb3V0aGVhc3QtMWUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hX2FwLXNvdXRoZWFzdC0xZCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX25scF9rZyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FfYXAtc291dGhlYXN0LTFmIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfc28iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9lY3NfYXNjZW5kX2FpMSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2llZl9kZXZpY2VfZGlyZWN0IiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZWNzX3ZncHVfZzUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9haXNfdmNtIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfY3NfYXJtX3BvYyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Vjc19yaSIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2FfY24tc291dGgtMWYiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hX2FwLXNvdXRoZWFzdC0xYyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX2Nsb3VkdGVzdF9wdF9od0luc3RhbmNlIiwiaWQiOiIwIn0seyJuYW1lIjoib3BfZ2F0ZWRfZGNzX2RjczJfZXUiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9hX3J1LW5vcnRod2VzdC0yYyIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX3VsYl9taWl0X3Rlc3QiLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9pZWZfcGxhdGludW0iLCJpZCI6IjAifSx7Im5hbWUiOiJvcF9nYXRlZF9PQlNfZmlsZV9wcm90b2NvbCIsImlkIjoiMCJ9LHsibmFtZSI6Im9wX2dhdGVkX1ZpZGVvX0NhbXB1cyIsImlkIjoiMCJ9XSwicHJvamVjdCI6eyJkb21haW4iOnsibmFtZSI6Imh3MDkyMzE0ODMiLCJpZCI6IjA2ZGQ1ZjkwMDEwMDI2NDUwZjNhYzAwZDQyNTVlN2EwIn0sIm5hbWUiOiJjbi1lYXN0LTIiLCJpZCI6IjA2ZGQ1ZjkwMGI4MDI2NDUyZjNkYzAwZDhiOTg3Nzk1In0sImlzc3VlZF9hdCI6IjIwMjAtMDItMDlUMTI6NDI6MDcuNzU3MDAwWiIsInVzZXIiOnsiZG9tYWluIjp7Im5hbWUiOiJodzA5MjMxNDgzIiwiaWQiOiIwNmRkNWY5MDAxMDAyNjQ1MGYzYWMwMGQ0MjU1ZTdhMCJ9LCJuYW1lIjoiaHcwOTIzMTQ4MyIsInBhc3N3b3JkX2V4cGlyZXNfYXQiOiIiLCJpZCI6IjA2ZGQ1ZjkxMDIwMDEwNWUxZjIyYzAwZGY5OGQ2N2RmIn19fTGCAcEwggG9AgEBMIGXMIGJMQswCQYDVQQGEwJDTjESMBAGA1UECAwJR3VhbmdEb25nMREwDwYDVQQHDAhTaGVuWmhlbjEuMCwGA1UECgwlSHVhd2VpIFNvZnR3YXJlIFRlY2hub2xvZ2llcyBDby4sIEx0ZDEOMAwGA1UECwwFQ2xvdWQxEzARBgNVBAMMCmNhLmlhbS5wa2kCCQDcsytdEGFqEDALBglghkgBZQMEAgEwDQYJKoZIhvcNAQEBBQAEggEAWFNpSLfMKhumzDgAoV6np9xaIXL5MbWC4ovA-dWOxl1dq5Bw6tuU8qECKVYJfz833IqVlXfZxXDyY-kav03Cei93Vtn2N9bzuNqXwOezc-sAuCdGzyPrqnCBU7gJAbxSLl0b2wiKH7fk4Q5UE4ZMldomSMK94vSUN5jDZ59SSI2wT5TTH0DD0Un48FrNS9efleJEY3MpDYx+uA684rM8n0Ch1hsjYWKgUCSbNQs8Pz6S3BRWshnG9Ywvt2jBuuRaSPe0c2cVl4AhhVPE83HTDlbI3XMvn44qm7WXc4+czOs7mhBt6gu1Ydu8p-zLry6tEgi0LfQHMvBMLLdgG5SAiA==");

            //request.setBody("{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\":{\"user\":{\"name\":\"hw09231483\",\"password\":\"fu@360124\",\"domain\":" + "{\"name\":\"hw09231483\"}}}},\"scope\":{\"project\":{\"name\":\"cn-east-2\"}}}}");


            //Add a body if you have specified the PUT or POST method. Special characters, such as the double quotation mark ("), contained in the body must be escaped.
            //request.setBody("demo");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return request;
    }
public static JsonRootBean executeAPI(CloudAPIDTO cloudAPIDTO) {
    Request request = getRequest(cloudAPIDTO);
    try {
        CloseableHttpClient client = null;
        //Sign the request.
        HttpRequestBase signedRequest = Client.sign(request);
        //Send the request.
        client = HttpClients.custom().setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                (x509CertChain, authType) -> true).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();

        HttpResponse response = client.execute(signedRequest);
        //Print the status line of the response.u8
        System.out.println(response.getStatusLine().toString());
        //Print the header fields of the response.
        Header[] resHeaders = response.getAllHeaders();
        for (Header h : resHeaders) {
            System.out.println(h.getName() + ":" + h.getValue());
        }
        //Print the body of the response.
        HttpEntity resEntity = response.getEntity();
        if (resEntity != null) {
            Gson gson = new Gson();
            String s=EntityUtils.toString(resEntity, "UTF-8");
            System.out.println(s);
            JsonRootBean jsonRootBean=gson.fromJson(s, JsonRootBean.class );
            return jsonRootBean;
            //jsonRootBean.getProjects().forEach(e -> {System.out.println(e.getId()+":"+e.getName()+":"+e.getLinks().getSelf()
            //);});

        }

    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    return null;
}

    public static void main(String[] args){




       // request.setUrl("https://iam.myhuaweicloud.com/v3/projects");
        CloudAPIDTO cloudAPIDTO=new CloudAPIDTO();
        cloudAPIDTO.setName("取项目列表");
        cloudAPIDTO.setRequestMethod("GET");
        cloudAPIDTO.setScheme("https");
        cloudAPIDTO.setServiceCode("iam");
        cloudAPIDTO.setEndpoint("myhuaweicloud.com");
        cloudAPIDTO.setRegionCode("");
        cloudAPIDTO.setProjectId("");
        cloudAPIDTO.setVersion("v3");
        cloudAPIDTO.setResourcePath("{scheme}://{serviceCode}.{endpoint}/{version}/projects");
//-------------分割线

//        cloudAPIDTO.setName("取区域列表");
//        cloudAPIDTO.setRequestMethod("GET");
//        cloudAPIDTO.setScheme("https");
//        cloudAPIDTO.setServiceCode("iam");
//        cloudAPIDTO.setEndpoint("myhuaweicloud.com");
//        cloudAPIDTO.setRegionCode("");
//        cloudAPIDTO.setProjectId("");
//        cloudAPIDTO.setVersion("v3");
//        cloudAPIDTO.setResourcePath("{scheme}://{serviceCode}.{endpoint}/{version}/regions");

//        //JsonRootBean(regions=null, projects=[Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=af-south-1, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06e05414970025d52f7bc00d01c160ac), id=06e05414970025d52f7bc00d01c160ac, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=ap-southeast-1, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06e0541491000f1b2f77c00dda8bc5d2), id=06e0541491000f1b2f77c00dda8bc5d2, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=ap-southeast-2, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06e054148f80268f2f67c00d9b0fc962), id=06e054148f80268f2f67c00d9b0fc962, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=ap-southeast-3, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06e0541492800f1a2f3dc00d3b5375ef), id=06e0541492800f1a2f3dc00d3b5375ef, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=cn-east-2, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06dd5f900b8026452f3dc00d8b987795), id=06dd5f900b8026452f3dc00d8b987795, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=cn-east-3, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06dd8c78db8026442f7bc00dda0ddb72), id=06dd8c78db8026442f7bc00dda0ddb72, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=cn-north-1, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06dd5f92020010582f96c00d80296593), id=06dd5f92020010582f96c00d80296593, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=cn-north-4, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06dd624480800f312f69c00d59d01cfd), id=06dd624480800f312f69c00d59d01cfd, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=cn-northeast-1, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/078a42c97b800f0a2f86c00db6675752), id=078a42c97b800f0a2f86c00db6675752, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=cn-south-1, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06dd62313580254b2ff4c00d76b706ce), id=06dd62313580254b2ff4c00d76b706ce, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=cn-southwest-2, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06e054149b0025ca2fc9c00da0852c28), id=06e054149b0025ca2fc9c00da0852c28, enabled=true), Project(domain_id=06dd5f90010026450f3ac00d4255e7a0, is_domain=false, parent_id=06dd5f90010026450f3ac00d4255e7a0, name=MOS, description=, links=Links(next=null, previous=null, self=https://iam.myhuaweicloud.com/v3/projects/06dd5f90058026452f3cc00df014f952), id=06dd5f90058026452f3cc00df014f952, enabled=true)], flavors=null)
//
//
//        //-------------分割线
//        cloudAPIDTO.setName("取规格列表");
//        cloudAPIDTO.setRequestMethod("GET");
//        cloudAPIDTO.setScheme("https");
//        cloudAPIDTO.setServiceCode("ecs");
//        cloudAPIDTO.setEndpoint("myhuaweicloud.com");
//        cloudAPIDTO.setRegionCode("cn-east-2");
//        cloudAPIDTO.setProjectId("06dd5f900b8026452f3dc00d8b987795");
//        //cloudAPIDTO.setRegionCode("af-south-1");
//        //cloudAPIDTO.setProjectId("06e05414970025d52f7bc00d01c160ac");
//        cloudAPIDTO.setVersion("v1");
//        cloudAPIDTO.setResourcePath("{scheme}://{serviceCode}.{regionCode}.{endpoint}/{version}/{projectId}/cloudservers/flavors");


        //-------------分割线
        cloudAPIDTO.setName("取镜像列表");
        cloudAPIDTO.setRequestMethod("GET");
        cloudAPIDTO.setScheme("https");
        cloudAPIDTO.setServiceCode("ims");
        cloudAPIDTO.setEndpoint("myhuaweicloud.com");
        cloudAPIDTO.setRegionCode("cn-east-2");
        cloudAPIDTO.setProjectId("");
        cloudAPIDTO.setVersion("v2");
        cloudAPIDTO.setResourcePath("{scheme}://{serviceCode}.{regionCode}.{endpoint}/{version}/cloudimages");

        //-------------分割线
        cloudAPIDTO.setName("取虚机列表");
        cloudAPIDTO.setRequestMethod("GET");
        cloudAPIDTO.setScheme("https");
        cloudAPIDTO.setServiceCode("ecs");
        cloudAPIDTO.setEndpoint("myhuaweicloud.com");
        //cloudAPIDTO.setRegionCode("cn-east-3");
        //cloudAPIDTO.setProjectId("06dd8c78db8026442f7bc00dda0ddb72");
        cloudAPIDTO.setRegionCode("cn-east-2");
        cloudAPIDTO.setProjectId("7b0043a1830b4bbda96d2dbd7cc2d571");
        cloudAPIDTO.setVersion("v1");
        cloudAPIDTO.setResourcePath("{scheme}://{serviceCode}.{regionCode}.{endpoint}/{version}/{projectId}/cloudservers/detail");



        cloudAPIDTO.setName("取虚机列表");
        cloudAPIDTO.setRequestMethod("GET");
        cloudAPIDTO.setScheme("https");
        cloudAPIDTO.setServiceCode("ecs");
        cloudAPIDTO.setEndpoint("zjbdos.com");
        cloudAPIDTO.setRegionCode("cn-yw-1");
        //cloudAPIDTO.setRegionCode("pro_browser");
        cloudAPIDTO.setProjectId("753c3b0e1c3248619f8c83572f74e8c4");
        //cloudAPIDTO.setProjectId("3f4b38eadc4c493ca28c4bc2ff947f42");
        //cloudAPIDTO.setRegionCode("af-south-1");
        //cloudAPIDTO.setProjectId("06e05414970025d52f7bc00d01c160ac");
        cloudAPIDTO.setVersion("v2.1");
        cloudAPIDTO.setResourcePath("{scheme}://{serviceCode}.{regionCode}.{endpoint}/{version}/{projectId}/servers/detail");
        //cloudAPIDTO.setResourcePath("{scheme}://{serviceCode}.{regionCode}.{endpoint}/{version}/{projectId}/cloudservers/detail");


        //cloudAPIDTO.setProjectId("123456");
       // cloudAPIDTO.setServerId("5792845");
        //String s="{scheme}://{serverCode}.{regionCode}.{endpoint}/{version}/{projectId}/servers/{serverId}/tags";
        //System.out.println(stringFormat(s,cloudAPIDTO));




//         cloudAPIDTO=new CloudAPIDTO();
//        cloudAPIDTO.setName("取项目列表");
//        cloudAPIDTO.setRequestMethod("GET");
//        cloudAPIDTO.setScheme("https");
//        cloudAPIDTO.setServiceCode("iam-apigateway-proxy");
//        cloudAPIDTO.setEndpoint("zjbdos.com");
//        cloudAPIDTO.setRegionCode("");
//        cloudAPIDTO.setProjectId("");
//        cloudAPIDTO.setVersion("v3");
//        cloudAPIDTO.setResourcePath("{scheme}://{serviceCode}.{endpoint}/{version}/auth/projects");
        //?limit=1000&offset=0
//        String s="{scheme}{serviceCode}{endpoint}";
        //stringFormat(s,cloudAPIDTO);
        String param=paramStringFormat(cloudAPIDTO.getResourcePath(),cloudAPIDTO);
        param=param.replaceFirst("&","?");
        //https://iam.myhuaweicloud.com/v3/auth/tokens
        System.out.println(param);
        //System.out.println(paramStringFormat(cloudAPIDTO.getResourcePath(),cloudAPIDTO));
//\{(.*?)\}
        System.out.println( cloudAPIDTO.getResourcePath().matches("."));
        System.out.println(cloudAPIDTO.getResourcePath().indexOf("{"));

        for(int i=0;i<10;i++){
            System.out.println(i);
            break;
        }
System.out.println("out");

        JsonRootBean jsonRootBean= executeAPI(cloudAPIDTO);



        JsonRootBean newJsonRootBean=new JsonRootBean();
        try {
            cloudAPIDTO.setPlatformCode("123");
            cloudAPIDTO.setRegionCode("456");
            getJsonRootBean(jsonRootBean,newJsonRootBean,cloudAPIDTO);
            //getJsonRootBean(jsonRootBean2,newJsonRootBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Config config= Config.newConfig().withSSLVerificationDisabled();
        newJsonRootBean.getServers().forEach(server -> {
           server.getAddresses().forEach((s1, addresses) -> {
                addresses.forEach(address -> {
                    System.out.println(address);
                });
            });
        });
        newJsonRootBean.getProjects().forEach(flavor -> {
            System.out.println(flavor.getPlatformCode());
        });

    }

    static void getJsonRootBean(Object fromObject, Object toObject, CloudAPIDTO cloudAPIDTO) throws Exception {

        Field[] fields = fromObject.getClass().getDeclaredFields();//Object是已经被赋值的对象实例
        for (Field field : fields) {
            if(field.isAnnotationPresent(BaseCloudAnnotation.class)){
                System.out.println(field);
            }
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (List.class.isAssignableFrom(field.getType())) {
                Type t = field.getGenericType();//获取list中的范型
                if (t instanceof ParameterizedType) {
                    ParameterizedType pt = (ParameterizedType) t;
                    //Class clz = (Class) pt.getActualTypeArguments()[0];//得到对象list中实例的类型
                    Object oList=field.get(fromObject);//oList是jsonRootBean1中的属性对象
                    if(oList!=null){
                        Object no=field.get(toObject);
                        if(no==null){
                            no=oList.getClass().newInstance();
                        }
                        Class clazz = oList.getClass();//获取到属性的值的Class对象
                        Method m= clazz.getDeclaredMethod("size");
                        int size = (Integer) m.invoke(field.get(fromObject));//调用list的size方法，得到list的长度
                        for (int i = 0; i < size; i++) {//遍历list，调用get方法，获取list中的对象实例
                            Method getM= clazz.getDeclaredMethod("get", int.class);
                            if(!getM.isAccessible()){
                                getM.setAccessible(true);
                            }



                            Object db1=getM.invoke(oList, i);//list中的实例对象
                            for (Field f : db1.getClass().getSuperclass().getDeclaredFields()) {
                                BaseCloudAnnotation c=f.getAnnotation(BaseCloudAnnotation.class);
                                if(c!=null){
                                    System.out.println(c);
                                    if( c.isRetainedField()) {
                                        Method getM5 = db1.getClass().getSuperclass().getDeclaredMethod("set" + StringUtils.capitalize(f.getName()), String.class);
                                        Method getM6=cloudAPIDTO.getClass().getMethod("get" + StringUtils.capitalize(f.getName()));
                                        getM5.invoke(db1, (String) getM6.invoke(cloudAPIDTO));//list中的实例对象
                                    }

                                    }

                            }





                            Method getM3= no.getClass().getDeclaredMethod("add",Object.class);
                            Object db3=getM3.invoke(no, db1);//list中的实例对象
                            //System.out.println(no);
                            Method getM4= toObject.getClass().getDeclaredMethod("set"+ StringUtils.capitalize(field.getName()),List.class);
                            Object db4=getM4.invoke(toObject, no);//list中的实例对象
                        }
                    }

                }

            }
        }
        System.out.println(toObject);
    }

}