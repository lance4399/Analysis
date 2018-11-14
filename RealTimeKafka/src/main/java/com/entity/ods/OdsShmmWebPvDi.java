package com.entity.ods;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:54
 **/

public class OdsShmmWebPvDi implements Serializable {

    private static final long serialVersionUID = 5331061669973777368L;
    private String logVersion;
    private String logTime;
    private String logTimeStamp;
    private String jsVersion;
    private String vstCookie;

    private String vstUserAgent;
    private String deviceResolution;
    private String pageUrl;

    private String pageReferUrl;

    /**
     * 页面类型:
     * 1:首页
     * 2:频道页
     * 3:文章页
     * 4:图集页
     * 5:评论
     * 6:列表
     * 7:标签
     */
    private String pageType;
    /**
     * spm码构成规则：logsource(日志来源).ChannelId(频道id).subChannelId(子频道id).wscrid(区块码).PsId(位置码)
     */
    private String spmCnt;
    /**
     * 当前页面的前一个跳转位置的spm，跳转url中带入的spm参数
     */
    private String spmPre;
    /**
     */
    private String scmCnt;

    private String pvid; //本次pv访问的id
    private Map<String, String> extMap; //扩展字段
    private String business;

    public OdsShmmWebPvDi() {
    }


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

    public String getLogTimeStamp() {
        return logTimeStamp;
    }

    public void setLogTimeStamp(String logTimeStamp) {
        this.logTimeStamp = logTimeStamp;
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


    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
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

    public String getPvid() {
        return pvid;
    }

    public void setPvid(String pvid) {
        this.pvid = pvid;
    }

    public Map<String, String> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, String> extMap) {
        this.extMap = extMap;
    }


    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    @Override
    public String toString() {
        return "OdsShmmWebPvDi{" +
                "logVersion='" + logVersion + '\'' +
                ", logTime='" + logTime + '\'' +
                ", logTimeStamp='" + logTimeStamp + '\'' +
                ", jsVersion='" + jsVersion + '\'' +
                ", vstCookie='" + vstCookie + '\'' +
                ", vstUserAgent='" + vstUserAgent + '\'' +
                ", deviceResolution='" + deviceResolution + '\'' +
                ", pageUrl='" + pageUrl + '\'' +
                ", pageReferUrl='" + pageReferUrl + '\'' +
                ", pageType='" + pageType + '\'' +
                ", spmCnt='" + spmCnt + '\'' +
                ", spmPre='" + spmPre + '\'' +
                ", scmPre='" + scmCnt + '\'' +
                ", pvid='" + pvid + '\'' +
                ", extMap=" + extMap +
                ", business='" + business + '\'' +
                '}';
    }
}

