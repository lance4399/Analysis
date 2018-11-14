package com.log_cleaner;

import com.utils.Constants;
import com.utils.LogUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:59
 **/

public class EtlBaseDataCleaner implements IBaseDataEtlCleaner {

    static Logger logger = LoggerFactory.getLogger(EtlBaseDataCleaner.class);

    public String formateLogTime(String timeLocal) throws Exception {
        SimpleDateFormat simpleDateFormat2Str = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return String.valueOf(simpleDateFormat2Str.format(Long.valueOf(timeLocal)));
    }

    public String getTimestamp(String timeLocation) throws Exception {
        String time = timeLocation.split("\\s")[0];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
        return String.valueOf(simpleDateFormat.parse(time).getTime());
    }


    public String trim(String value) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(value) || value.trim().equals("")) {
            return Constants.CLEANED_LOG_DEFAULT_VALUE;
        } else {
            return value;
        }
    }

    /**
     * 得到url的所有访问参数的map
     *
     * @param urlStr 访问的url字符串
     * @return
     */
    public Map<String, String> getRequestArgsMap(String urlStr) throws Exception {
        String requestQuery = "";
        String requestPath = "";
        if (urlStr.contains("?")) {
            requestQuery = urlStr.substring(urlStr.indexOf("?") + 1);
            if (urlStr.contains("/")) {
                requestPath = urlStr.substring(urlStr.indexOf("/"), urlStr.indexOf("?"));
            }
        } else {
            requestPath = urlStr;
        }
        Map<String, String> requestArgsMap = LogUtils.formatQueryToMap(requestQuery);
        if (requestArgsMap == null || requestArgsMap.size() == 0) {
            return LogUtils.formatQueryToMap(requestQuery);
        } else {
            if (requestArgsMap.containsKey("url")) {
                String url = LogUtils.getURLDecoderString(requestArgsMap.get("url"));
                requestArgsMap.put("url", url);
            }
            if (requestArgsMap.containsKey("r")) {
                String refer = LogUtils.getURLDecoderString(requestArgsMap.get("f"));
                requestArgsMap.put("refer", refer);
            }
            return requestArgsMap;
        }
    }

    /**
     * 得到url的所有访问参数的map
     *
     * @param extStr 访问的url字符串
     * @return
     */
    public Map<String, String> formateExtMap(String extStr) throws Exception {
        Map<String, String> extMap = new HashMap<>();
        if (extStr.contains(";")) {
            String[] kvs = extStr.split(";");
            for (String kvstr : kvs) {
                String[] kv = kvstr.split(":");
                if (kv.length == 2) {
                    String key = kv[0];
                    String value = kv[1];
                    extMap.put(key, value);
                }
            }
        }
        return extMap;
    }


    public String trimGetRequest(String getRequest) {
        if (getRequest.contains("HEAD")) {
            getRequest = getRequest.replace("HEAD", "GET");
        }
        try {
            getRequest = URLDecoder.decode(getRequest, "UTF-8");
        } catch (Exception e) {
            logger.error("trimedGetRequest Error:" + e.getMessage());
            return getRequest;
        }
        return getRequest;
    }

    protected String appendUrl(String url, String paramName, String paraValue) {
        try {
            String appendUrl = new URIBuilder(url).addParameter(paramName, paraValue).build().toString();
            return appendUrl;
        } catch (URISyntaxException e) {
            logger.error(e.getMessage());
        }
        return url;
    }

    public static String getNewsIdAndMediaIdByPath(String path) {
        String newsId = Constants.CLEANED_LOG_DEFAULT_VALUE;
        String mediaId = Constants.CLEANED_LOG_DEFAULT_VALUE;
        try {
            Matcher newsAndMediaMatcher = Pattern.compile(Constants.NEWS_MEDIA_RE).matcher(path);
            Matcher newsAndMediaMatcherForSubject = Pattern.compile(Constants.SUBJECT_NEWS_MEDIA_RE).matcher(path);
            if (newsAndMediaMatcher.find()) {
                newsId = newsAndMediaMatcher.group(1);
                mediaId = newsAndMediaMatcher.group(2);
                return mediaId + "_" + newsId;
            } else if (newsAndMediaMatcherForSubject.find()) {
                if (Pattern.compile(Constants.PAGE_TYPE_RE.AUTHOR_PAGE_TYPE).matcher(path).find()) {
                    mediaId = newsAndMediaMatcherForSubject.group(1);
                } else {
                    newsId = newsAndMediaMatcherForSubject.group(1);
                }
                return mediaId + "_" + newsId;
            } else {
                return mediaId + "_" + newsId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaId + "_" + newsId;
    }

    /**
     * 老的日志参数m为页面类型，经过考证研究，m参数全部都为0，故废弃originalPreLog 里的pageType参数
     * 一级页面类型
     */
    @Override
    public String getPageType(String urlStr) throws Exception {
        String pageType = "99";

        URL url = new URL(urlStr);
        String host = url.getHost();
        String path = url.getPath();

        if ("99".equalsIgnoreCase(pageType) || Constants.CLEANED_LOG_DEFAULT_VALUE.equalsIgnoreCase(pageType)) {
            //首页
            if (("/".equalsIgnoreCase(path) && "xxx.com".equalsIgnoreCase(host))
                    || ("/".equalsIgnoreCase(path) && "www.xxx.com".equalsIgnoreCase(host))
                    || ("popup.xxx.com").equalsIgnoreCase(host)) {
                pageType = "1";
            }
            //频道页
            if (Pattern.compile(Constants.PAGE_TYPE_RE.CHANNEL_PAGE_TYPE).matcher(path).find() ||
                    (Pattern.compile("\\S.xxx.com").matcher(host).find() && "/".equalsIgnoreCase(path) && !"www.xxx.com".equalsIgnoreCase(host) && !"m.sohu.com".equalsIgnoreCase(host))) {
                pageType = "2";
            }
            //文章页面
            if (Pattern.compile(Constants.PAGE_TYPE_RE.ARTICLE_PAGE_TYPE).matcher(path).find()) {
                pageType = "3";
            }
            //作者页面 包含：手搜：m.xxx.com/media/  PC：mp.xxx.com/profile
            if (Pattern.compile(Constants.PAGE_TYPE_RE.AUTHOR_PAGE_TYPE).matcher(path).find()) {
                pageType = "4";
            }
            if (Pattern.compile(Constants.PAGE_TYPE_RE.SUBJECT_PAGE_TYPE).matcher(path).find()) {
                pageType = "5";
            }
        }
        return pageType;

    }

    public String getVstIp(String vstIps) {
        String[] vstIpLst = vstIps.split(",");
        if (vstIpLst.length >= 1) {
            return vstIpLst[0];
        } else {
            return vstIps;
        }
    }


    public String getPvid(String spmCnt) {
        String pvid = Constants.CLEANED_LOG_DEFAULT_VALUE;
        String[] spmCntArr = spmCnt.split("\\.");
        if (spmCntArr.length == 5) {
            pvid = spmCntArr[4];
        }
        return pvid;
    }

    public void removeCurentKeyFromExtMap(Map<String, String> extMap, String key) {
        if (extMap.containsKey(key)) {
            extMap.remove(key);
        }
    }


}

