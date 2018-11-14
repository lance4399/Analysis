package com.etl;

import org.apache.commons.lang3.StringUtils;

public class GetPvIdParser {

    public String run(String spm) {
        String result = "";
        if (StringUtils.isNotBlank(spm)) {
            String[] items = spm.split("\\.");
            if(items.length == 5) {
                result = items[items.length - 1];
            }
        }
        return result;
    }
}
