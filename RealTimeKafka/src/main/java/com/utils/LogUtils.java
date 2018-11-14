package com.utils;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 11:00
 **/
public class LogUtils {
    private final static String ENCODE = "UTF-8";

    static Logger logger = LoggerFactory.getLogger(LogUtils.class);


    public static boolean parameterIsValide(String value) {
        return value != null && !value.equalsIgnoreCase("-") && !value.equalsIgnoreCase("");
    }

    public static boolean parameterIsValideForLong(String value) {
        long result;
        try {
            result = Long.parseLong(value);
        } catch (NumberFormatException e) {
            return false;
        }
        if (result > 0)
            return true;
        return false;
    }

    public static boolean parameterIsValideForFloat(String value) {
        float result;
        try {
            result = Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return false;
        }
        if (result > 0)
            return true;
        return false;
    }

    /**
     * query格式化为json格式
     *
     * @param query
     * @return
     */
    public static Map<String, String> formatQueryToMap(String query) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            if (StringUtils.isNotEmpty(query)) {
                String[] filds = query.split("&");
                for (String fild : filds) {
                    String[] keyValue = fild.split("=");
                    if (keyValue.length == 2) {
                        map.put(keyValue[0], keyValue[1]);
                    }
                    if (keyValue.length == 1) {
                        map.put(keyValue[0], null);
                    }
                }
            }
            return map;
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 根据参数名称获取参数值
     *
     * @param query eg:a=1&_smuid=fdsewire&c=3
     * @param key   eg:_smuid
     * @return eg:fdsewire
     */
    public static String getValFromQueryByKey(String query, String key) {
        try {
            if (parameterIsValide(query)) {
                String[] filds = query.split("&");
                for (String fild : filds) {
                    String[] keyValue = fild.split("=");
                    if (keyValue.length == 2
                            && keyValue[0].equalsIgnoreCase(key)) {
                        return keyValue[1];
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * URL 解码
     */
    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * URL 转码
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, ENCODE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * JSON TO MAP
     */
    public static Map<String, String[]> json2MapArrayList(String jsonStr) {

        Map<String,  String[]> map = new HashMap<String,  String[]>();
        Map<String, String[]> result_map = new HashMap<String, String[]>();
        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(jsonStr, new TypeReference<HashMap<String,  String[]>>() {
            });
            Iterator entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                String[] valueArray;
                Map.Entry entry = (Map.Entry) entries.next();
                String key = (String) entry.getKey();
                String[] valueArr = ( String[]) entry.getValue();
                for (String value:valueArr) {
                    if (value.contains(",")) {
                        String[] values = value.split(",");
                        valueArray = values;
//                    valueArray = new String[values.length-1];
//                    for (int i=0; i<values.length ;i++) {
//                        valueArray[i]=values[i];
//                    }
                        result_map.put(key, valueArray);
                    }
//                    else if (value.contains(";")) {
//                        String[] values = value.split(";");
//                        valueArray = values;
//                        result_map.put(key, valueArray);
//                    }
                    else {
                        valueArray = valueArr;
                        result_map.put(key, valueArray);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_map;
    }

    /**
     * JSON TO MAP
     */
    public static Map<String, String> json2Map(String jsonStr) {

        Map<String, String> map = new HashMap<String, String>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(jsonStr, new TypeReference<HashMap<String, String>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * MAP TO JSON
     */
    public static String map2Json(Map<String, String> mapArg) {
        String json = "";
        try {

            ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(mapArg);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }


}

