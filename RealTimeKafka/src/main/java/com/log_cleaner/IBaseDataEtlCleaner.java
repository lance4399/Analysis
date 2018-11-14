package com.log_cleaner;

public interface IBaseDataEtlCleaner {
    String getPageType(String url) throws Exception;

    String formateLogTime(String timeLocation) throws Exception;

    String trim(String value);
}
