package com.utils;

import java.util.HashSet;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:53
 **/

public class Constants {
    public static String CLEANED_LOG_DEFAULT_VALUE = "-";

    public static char ORIGINAL_SEPARATOR = '\u0003';
    /**
     * 利用正则表达式去匹配newid和mediaId
     */
    public final static String NEWS_MEDIA_RE = "^/(?:news/)?(?:o|a)/(\\d+)_(\\d+)\\/?$";
    public final static String CM_NEWS_MEDIA_RE = "^/cm/(\\d+)_(\\d+)";
    public final static String SUBJECT_NEWS_MEDIA_RE = "^/\\S*/(\\d+)/?$";
    public final static String PC_OLD_CMS_NEWS_RE = ".*?\\.sohu\\.com/\\d{8}/n(\\d+)\\.shtml$";

    /**
     * 利用正则判断是不是老CMS系统提供的文章
     */
    public final static String CMS_ARTICLE_NEWS_RE = "^/n/(\\d+)";

    public static final class PAGE_TYPE_RE {
        public static final String CHANNEL_PAGE_TYPE = "^/c|(ch)/(\\d+)/?$"; //频道页
        public static final String ARTICLE_PAGE_TYPE = "^/n|a|o|(zo)|(news/a)?/(\\d+)/?$"; // 正文页
        public static final String PICTURE_PAGE_TYPE = "^/(picture)|p/(\\d+)/?$"; // 图集页
        public static final String TAG_PAGE_TYPE = "^/tag/(\\d+)/?$"; //标签页
        public static final String AUTHOR_PAGE_TYPE = "^/(media)|(profile)/(\\d+)/?$";//标签页
        public static final String SUBJECT_PAGE_TYPE = "^/subject/(\\d+)/?$" ;//标签页
    }



}

