package com.log_cleaner;

import com.entity.ods.OdsShmmWebPvDi;
import com.entity.rawlog.OriginalPvLogBean;
import com.utils.Constants;
import com.utils.LogUtils;
import com.utils.ParseLogUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:58
 **/

public class EtlODSPvLogCleaner extends EtlBaseDataCleaner implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(EtlODSPvLogCleaner.class);

    public OdsShmmWebPvDi cleanODSPvlog(String logStr) {
        OdsShmmWebPvDi logsODS = new OdsShmmWebPvDi();

        OriginalPvLogBean originalPvLog = new OriginalPvLogBean(logStr);
        try {
            String jsVersion = originalPvLog.getJsVersion();
//            if ("test".equalsIgnoreCase(jsVersion)) {
//                return null;
//            }

            String logTimeStamp = originalPvLog.getLogTime();
            String logTime = formateLogTime(logTimeStamp);
            String suv = originalPvLog.getVstCookie();

            String urlCaseSensitive = originalPvLog.getPageUrl();
            String referCaseSensitive = originalPvLog.getPageReferUrl();

            //把有的url只有host的部分，把这一部分的url拼接完整
            if (!urlCaseSensitive.startsWith("http")
                    && !urlCaseSensitive.startsWith("https")
//                    && (!StringUtils.isEmpty(urlCaseSensitive))
                    ) {
                urlCaseSensitive = String.format("%s%s", "http://", urlCaseSensitive);
            }
            /**
             * 处理特殊的x.m.xxx.com
             */
            if (!StringUtils.isEmpty(urlCaseSensitive)) {
                URL url = new URL(urlCaseSensitive);
                if (url.getHost().equalsIgnoreCase("x.m.xxx.com")) {
                    urlCaseSensitive = urlCaseSensitive.replace("x.m.xxx.com", "x.m.xxx.com");
                }
            }

            String pageType = getPageType(urlCaseSensitive);

            String spmPre = originalPvLog.getSpmPre();
            String scmCnt = originalPvLog.getScmCnt();
            Map<String, String> urlRequestArgsMap = getRequestArgsMap(urlCaseSensitive);
            if (!LogUtils.parameterIsValide(spmPre)) {
                if (urlRequestArgsMap.containsKey("spm")) {
                    spmPre = urlRequestArgsMap.get("spm");
                    removeCurentKeyFromExtMap(urlRequestArgsMap, "spm");

                } else {
                    spmPre = Constants.CLEANED_LOG_DEFAULT_VALUE;
                }
            }
            if (!LogUtils.parameterIsValide(scmCnt)) {
                if (urlRequestArgsMap.containsKey("scm")) {
                    scmCnt = urlRequestArgsMap.get("scm");
                    removeCurentKeyFromExtMap(urlRequestArgsMap, "scm");

                } else {
                    scmCnt = Constants.CLEANED_LOG_DEFAULT_VALUE;
                }
            }

            Map<String, String> extMap = formateExtMap(originalPvLog.getExtMap());
            if (urlRequestArgsMap != null && urlRequestArgsMap.size() > 0) {
                Iterator<Map.Entry<String, String>> entries = urlRequestArgsMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, String> entry = entries.next();
                    extMap.put(entry.getKey(), entry.getValue());
                }
            }
            String business = ParseLogUtils.getBusiness(urlCaseSensitive);
            String pvid = getPvid(originalPvLog.getSpmCnt());

            /**
             *  logsODS 修正无效值为"-"
             */
            logsODS.setLogVersion(originalPvLog.getLogVersion());
            logsODS.setLogTime(trim(logTime));
            logsODS.setLogTimeStamp(trim(logTimeStamp));
            logsODS.setJsVersion(originalPvLog.getJsVersion());
            logsODS.setVstCookie(trim(suv));
            /**
             * 设置ua
             */
            logsODS.setVstUserAgent(originalPvLog.getVstUserAgent());
            /**
             * 分辨率
             */
            logsODS.setDeviceResolution(originalPvLog.getDeviceResolution());
            logsODS.setPageUrl(trim(urlCaseSensitive));
            logsODS.setPageReferUrl(trim(referCaseSensitive));
            logsODS.setPageType(pageType);
            logsODS.setSpmCnt(originalPvLog.getSpmCnt());
            logsODS.setSpmPre(spmPre);
            logsODS.setScmCnt(scmCnt);
            logsODS.setPvid(trim(pvid));
            logsODS.setExtMap(extMap);
            logsODS.setBusiness(business);
            return logsODS;
        } catch (Exception e) {
            logger.error(" clean logsODS error " + e.getMessage());
            logger.error("=====================prelog:{}===================", logStr);
            logger.error("=====================prelog:{}===================", originalPvLog.toString());
        }
        return null;
    }

}
