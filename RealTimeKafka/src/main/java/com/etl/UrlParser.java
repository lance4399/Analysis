package com.etl;
import java.net.URLDecoder;

/**
 * 将URL解码为code类型的数据
 * 输出结果类型：String
 * 输入参数 code可以为"UTF-8","GBK"等
 * */

public class UrlParser {
    private String urldecode = null;
    private String code = "GBK";

    public String decoder(String urlStr, String srcCode) {
        if (urlStr == null) {
            return null;
        }
        if (srcCode != null) {
            code = srcCode;
        }
        try {
            urldecode = decoderUtil(urlStr, code);
            return urldecode;
        } catch (Exception e) {
            return null;
        }
    }

    private String decoderUtil(String urlStr, String code) {
        if (urlStr == null || code == null) {
            return null;
        }
        try {
            urlStr = URLDecoder.decode(urlStr, code);
        } catch (Exception e) {
            return null;
        }
        return urlStr;
    }

}
