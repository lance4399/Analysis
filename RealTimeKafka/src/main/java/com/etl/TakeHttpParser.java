package com.etl;

/**
 *
 * 去掉url 中的 http:// 或 https://
 *
 */
public class TakeHttpParser {

    public String run(String str) {
        String result = "";
        String url = str.trim().toLowerCase();

        if(url.length() >=8) {
            String http = url.substring(0,7);
            if(("http://").equals(http)) {
                result = url.substring(7,url.length());
            } else if(("https://").equals(url.substring(0,8))) {
                result = url.substring(8,url.length());
            } else {
                result = url;
            }
        } else {
            result = "";
        }
        return result;
    }
}
