package com.utils;

import com.enums.BusinessEnum;

import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ParseLogUtils extends LogUtils {

    public static String getNewsIdAndMediaId(String urlStr) throws Exception {
        String newsId = Constants.CLEANED_LOG_DEFAULT_VALUE;
        String mediaId = Constants.CLEANED_LOG_DEFAULT_VALUE;
        URL url = new URL(urlStr);
        Matcher newsAndMediaMatcher = Pattern.compile(Constants.NEWS_MEDIA_RE).matcher(url.getPath());
        Matcher newsAndMediaMatcherForSubject = Pattern.compile(Constants.SUBJECT_NEWS_MEDIA_RE).matcher(url.getPath());
        Matcher pcOldCmsMatcher = Pattern.compile(Constants.PC_OLD_CMS_NEWS_RE).matcher(urlStr);
        Matcher cmNewsAndMediaMatcher = Pattern.compile(Constants.CM_NEWS_MEDIA_RE).matcher(url.getPath());
        if (newsAndMediaMatcher.find()) {
            newsId = newsAndMediaMatcher.group(1);
            mediaId = newsAndMediaMatcher.group(2);
            return mediaId + "_" + newsId;
        } else if (newsAndMediaMatcherForSubject.find() && //匹配专题页、图库页、作者页，并且取到内容id，媒体id
                !(Pattern.compile(Constants.PAGE_TYPE_RE.CHANNEL_PAGE_TYPE).matcher(url.getPath()).find())
                ) {
            if (Pattern.compile(Constants.PAGE_TYPE_RE.AUTHOR_PAGE_TYPE).matcher(url.getPath()).find()) {
                mediaId = newsAndMediaMatcherForSubject.group(1);
            } else {
                if (Pattern.compile(Constants.CMS_ARTICLE_NEWS_RE).matcher(url.getPath()).find()) {
                    newsId = "cms" + newsAndMediaMatcherForSubject.group(1);
                } else {
                    newsId = newsAndMediaMatcherForSubject.group(1);
                }
            }
            return mediaId + "_" + newsId;
        } else if (pcOldCmsMatcher.find()) {
            /**
             * 匹配老的CMS系统问文章id
             */
            newsId = "cms" + pcOldCmsMatcher.group(1);
        } else if (cmNewsAndMediaMatcher.find()) {
            /**
             * 匹配品论也面的 自媒体id 和 文章id
             */
            newsId = cmNewsAndMediaMatcher.group(1);
            mediaId = cmNewsAndMediaMatcher.group(2);
        } else {
            return mediaId + "_" + newsId;
        }
        return mediaId + "_" + newsId;
    }


    public static String getBusiness(String urlStr) throws Exception {
        String business = Constants.CLEANED_LOG_DEFAULT_VALUE;
        URL url = new URL(urlStr);
        String host = url.getHost();
        String path = url.getPath();
        if (Pattern.compile("m\\.sohu\\.com").matcher(host).find() && (!host.contains("film"))
                || Pattern.compile("book\\.m\\.sohu\\.com").matcher(host).find()
                || Pattern.compile("(//S\\.)?m\\.auto\\.sohu\\.com").matcher(host).find()
                || Pattern.compile("(quan|haochebang|mobile|qabot|dealer|i|autoapp|saa)\\.auto\\.sohu\\.com").matcher(host).find()
                || Pattern.compile("m.2sc.sohu.com").matcher(host).find()
                || (Pattern.compile("00.auto.sohu.com").matcher(host).find() && !Pattern.compile("^/d/(\\S)?").matcher(path).find())
                || (Pattern.compile("s.auto.sohu.com").matcher(host).find() && Pattern.compile("^/search/wap").matcher(path).find())
                || (Pattern.compile("m.zhidao.sohu.com").matcher(host).find())
                ) {
            business = BusinessEnum.WAP.getValue();
        } else if ("popup.sohu.com".equalsIgnoreCase(host)) {
            business = BusinessEnum.POPUP.getValue();
        } else {
            business = BusinessEnum.PC.getValue();
        }
        return business;
    }


}