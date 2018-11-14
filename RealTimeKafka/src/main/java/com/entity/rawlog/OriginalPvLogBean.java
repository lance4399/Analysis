package com.entity.rawlog;

import com.utils.Constants;

import java.io.Serializable;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:51
 **/

public class OriginalPvLogBean  implements Serializable {
    private static final long serialVersionUID = 5962055013268605741L;
    private String logVersion;  //日志版本号
    private String logTime;     //日志采集的时间戳
    private String jsVersion; //采集js版本号
    private String vstCookie; //用户cookie标识（SUV）
    private String vstUserAgent; //用户访问的use_agent
    private String deviceResolution; //设备分辨率
    private String pageUrl; //用户当前访问的页面url
    private String pageReferUrl; //当前页面的前一个页面url
    private String pageYyId; //搜狗弹窗页面yyid
    private String extMap; //扩展字段
    /** spm码构成规则：站点.页面.区块.点位.pvId */
    private String spmCnt;
    private String spmPre;
    private String scmCnt;

    public String getLogVersion() {
        return logVersion;
    }

    public void setLogVersion(String logVersion) {
        this.logVersion = logVersion;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getJsVersion() {
        return jsVersion;
    }

    public void setJsVersion(String jsVersion) {
        this.jsVersion = jsVersion;
    }

    public String getVstCookie() {
        return vstCookie;
    }

    public void setVstCookie(String vstCookie) {
        this.vstCookie = vstCookie;
    }

    public String getVstUserAgent() {
        return vstUserAgent;
    }

    public void setVstUserAgent(String vstUserAgent) {
        this.vstUserAgent = vstUserAgent;
    }

    public String getDeviceResolution() {
        return deviceResolution;
    }

    public void setDeviceResolution(String deviceResolution) {
        this.deviceResolution = deviceResolution;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageReferUrl() {
        return pageReferUrl;
    }

    public void setPageReferUrl(String pageReferUrl) {
        this.pageReferUrl = pageReferUrl;
    }

    public String getPageYyId() {
        return pageYyId;
    }

    public void setPageYyId(String pageYyId) {
        this.pageYyId = pageYyId;
    }

    public String getExtMap() {
        return extMap;
    }

    public void setExtMap(String extMap) {
        this.extMap = extMap;
    }

    public String getSpmCnt() {
        return spmCnt;
    }

    public void setSpmCnt(String spmCnt) {
        this.spmCnt = spmCnt;
    }

    public String getSpmPre() {
        return spmPre;
    }

    public void setSpmPre(String spmPre) {
        this.spmPre = spmPre;
    }

    public String getScmCnt() {
        return scmCnt;
    }

    public void setScmCnt(String scmCnt) {
        this.scmCnt = scmCnt;
    }

    private String[] columnValues;

    public OriginalPvLogBean(String log) {
        if (log == null || log.equalsIgnoreCase("")) {
            this.columnValues = null;
        }
        String[] logs = log.split(String.valueOf(Constants.ORIGINAL_SEPARATOR),13);
        if (logs.length == 13) {
            columnValues = new String[13];
            this.columnValues = logs;
        } else {
            this.columnValues = null;
        }
        this.init();
    }

    private void init() {
        if (this.columnValues == null)
            return;
        this.setLogVersion(columnValues[0]);
        this.setLogTime(columnValues[1]);
        this.setJsVersion(columnValues[2]);
        this.setVstCookie(columnValues[3]);
        this.setVstUserAgent(columnValues[4]);
        this.setDeviceResolution(columnValues[5]);
        this.setPageUrl(columnValues[6]);
        this.setPageReferUrl(columnValues[7]);
        this.setPageYyId(columnValues[8]);
        this.setSpmCnt(columnValues[9]);
        this.setSpmPre(columnValues[10]);
        this.setScmCnt(columnValues[11]);
        this.setExtMap(columnValues[12]);
    }

    @Override
    public String toString() {

        return "OriginalPvLog{" +
                "logVersion='" + getLogVersion() + '\'' +
                ", logTime='" + getLogTime() + '\'' +
                ", jsVersion='" + getJsVersion() + '\'' +
                ", vstCookie='" + getVstCookie() + '\'' +
                ", vstUserAgent='" + getVstUserAgent() + '\'' +
                ", pageUrl='" + getPageUrl() + '\'' +
                ", pageReferUrl='" + getPageReferUrl() + '\'' +
                ", pageYyId='" + getPageYyId() + '\'' +
                ", deviceResolution='" +  + '\'' +
                ", spmCnt='" + spmCnt + '\'' +
                ", spmPre='" + spmPre + '\'' +
                ", scmPre='" + scmCnt + '\'' +
                ", extMap=" + getExtMap() +
                '}';
    }
}
