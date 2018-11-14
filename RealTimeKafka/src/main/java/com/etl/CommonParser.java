package com.etl;

import org.apache.commons.lang3.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class CommonParser {

    private static final SimpleDateFormat sf = new SimpleDateFormat("HH");

    /**
     * 求字符串的子串
     * @param string
     * @param startIndex
     * @param endIndex
     * @return
     */
    public String childString(String string, String startIndex, String endIndex) {
        String result = "";
        try {
            int startIdx = Integer.valueOf(startIndex);
            int endIdx = Integer.valueOf(endIndex);
            if(StringUtils.isNotBlank(string) && startIdx >= 0 && endIdx > 0 && endIdx <= string.length()) {
                result = string.substring(startIdx,endIdx);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * 将时间分配切成每5分钟段
     * @param time
     * @return
     */
    public String fiveMinuteFun (String time){
        String result = "";
        long timeLong = 0;
        try {
            timeLong = Long.valueOf(time);
        } catch (Exception e) {
            result = "";
        }
        Date date = new Date(timeLong);
        String hourStr = sf.format(date);
        result = hourStr ;
        return result;
    }

    /**
     * 把空值转换成空字符串
     * 空值包括 "" ，空对象，字符串 “null” 和 “NULL”
     * @param string
     * @return
     */
    public String null2Blank(String string) {
        String result = "";
        if(StringUtils.isNotBlank(string) || "null".equals(string.toLowerCase())) {
            result = "";
        } else {
            result = string;
        }
        return result;
    }

    /**
     * 查找字符串是否包含子串
     * 包含返回 “0”, 不包含返回 “-1”
     * strContains(string,subString)
     * @param string
     * @param subStr
     * @return
     */
    public String stringContains(String string , String subStr) {
        String result = "-1";
        if(StringUtils.isNotBlank(string) && StringUtils.isNotBlank(subStr)) {
            result = string.contains(subStr) ? "0" : "-1";
        }
        return result;
    }

    /**
     * 得到子串在整个字符串中的顺序(从头匹配或从尾匹配)第一个位置的值
     * strIndex(string,subString,'0/-1')
     * @param string
     * @param subStr
     * @return
     */
    public String stringIndex(String string, String subStr) {
        String result = "";
        if(StringUtils.isNotBlank(string) && StringUtils.isNotBlank(subStr)) {
            try{
                result = String.valueOf(string.indexOf(subStr));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            result = "-1";
        }
        return result;
    }

    public String stringIndex(String string, String subStr, String Type) {
        String result = "";
        if(StringUtils.isNotBlank(string) && StringUtils.isNotBlank(subStr)) {
            try{
                int type = Integer.valueOf(Type);
                if(type == -1) {
                    result = String.valueOf(string.lastIndexOf(subStr));
                } else {
                    result = String.valueOf(string.indexOf(subStr));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            result = "-1";
        }
        return result;
    }

    /**
    * MAP类型数据转为string
    * */
    public String mapToString(Map<String,String> paraMap){
        if (paraMap == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, String> entry : paraMap.entrySet()) {
            if(i > 0) sb.append(";");
            sb.append(entry.getKey()).append("=").append(entry.getValue());
            i++;
        }
        return sb.toString();
    }
}
