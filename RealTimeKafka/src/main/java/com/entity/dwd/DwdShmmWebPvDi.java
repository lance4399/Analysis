package com.entity.dwd;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 10:55
 **/

public class DwdShmmWebPvDi implements Serializable {
    private static final long serialVersionUID = 8079453152082041031L;

    private Integer logVersion;    //	日志版本号
    private Long logTime;    //	日志采集时间
    private Long logTimeStamp;    //	日志采集时间戳yyyyMMddHHmmssSSS
    private String jsVersion;
    private String vstCookie;


    private Integer vstDeviceId;
    private Integer vstBrowserId;
    private String vstDeviceType;

    private String vstDeviceModel;
    private String vstDeviceResolution;
    private String vstOsType;

    private String vstBrowserType;

    private String vstBrowserVersion;
    private String pagePvId;    //	页面pv展现唯一id
    private String pageUrl;    //	当前页面的完整URL
    private String pageUrlNoParams;
    private String pageUrlHost;

    private Integer pageTypeId;    //	页面类型id
    private String pageType;    //	页面类型

    private String referUrl;
    private String referUrlNoParams;
    private String referUrlHost;    //	refer页面的host
    private String referPvId;
    private String sourceId;

    private String spmCnt;    //	当前页面的spm码
    private String spmPre;    //	前一个页面的spm码
    private String scmCnt;    //	当前页面的scm码
    private String spmCntA;    //	当前页面的spm码的a标识

    private String spmCntB;    //	当前页面的spm码的b标识
    private String spmPreA;    //	前一个页面的spm码的a标识
    private String spmPreB;    //	前一个页面的spm码的b标识
    private String spmPreC;    //	前一个页面的spm码的c标识
    private String spmPreD;    //	前一个页面的spm码的d标识

    private String scmCntA;    //	当前页面的scm码的a标识
    private String scmCntB;    //	当前页面的scm码的b标识
    private String scmCntC;    //	当前页面的scm码的c标识
    private String scmCntD;    //	当前页面的scm码的d标识

    private Integer isCrawler;
    private Double isAbnormal;
    private String abnormalRule;
    private String vstUserAgent;    //	用户UA
    private String ext;    //	扩展字段

    private Integer isNewUser;   //1：新用户；0老用户
    private String dt;
    private String business;

    public Integer getLogVersion() {
        return logVersion;
    }

    public void setLogVersion(Integer logVersion) {
        this.logVersion = logVersion;
    }

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    public Long getLogTimeStamp() {
        return logTimeStamp;
    }

    public void setLogTimeStamp(Long logTimeStamp) {
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


    public Integer getVstDeviceId() {
        return vstDeviceId;
    }

    public void setVstDeviceId(Integer vstDeviceId) {
        this.vstDeviceId = vstDeviceId;
    }


    public Integer getVstBrowserId() {
        return vstBrowserId;
    }

    public void setVstBrowserId(Integer vstBrowserId) {
        this.vstBrowserId = vstBrowserId;
    }

    public String getVstDeviceType() {
        return vstDeviceType;
    }

    public void setVstDeviceType(String vstDeviceType) {
        this.vstDeviceType = vstDeviceType;
    }

    public String getVstDeviceModel() {
        return vstDeviceModel;
    }

    public void setVstDeviceModel(String vstDeviceModel) {
        this.vstDeviceModel = vstDeviceModel;
    }

    public String getVstDeviceResolution() {
        return vstDeviceResolution;
    }

    public void setVstDeviceResolution(String vstDeviceResolution) {
        this.vstDeviceResolution = vstDeviceResolution;
    }

    public String getVstOsType() {
        return vstOsType;
    }

    public void setVstOsType(String vstOsType) {
        this.vstOsType = vstOsType;
    }


    public String getVstBrowserType() {
        return vstBrowserType;
    }

    public void setVstBrowserType(String vstBrowserType) {
        this.vstBrowserType = vstBrowserType;
    }

    public String getVstBrowserVersion() {
        return vstBrowserVersion;
    }

    public void setVstBrowserVersion(String vstBrowserVersion) {
        this.vstBrowserVersion = vstBrowserVersion;
    }

    public String getPagePvId() {
        return pagePvId;
    }

    public void setPagePvId(String pagePvId) {
        this.pagePvId = pagePvId;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrlNoParams() {
        return pageUrlNoParams;
    }

    public void setPageUrlNoParams(String pageUrlNoParams) {
        this.pageUrlNoParams = pageUrlNoParams;
    }

    public String getPageUrlHost() {
        return pageUrlHost;
    }

    public void setPageUrlHost(String pageUrlHost) {
        this.pageUrlHost = pageUrlHost;
    }

    public Integer getPageTypeId() {
        return pageTypeId;
    }

    public void setPageTypeId(Integer pageTypeId) {
        this.pageTypeId = pageTypeId;
    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }


    public String getReferUrl() {
        return referUrl;
    }

    public void setReferUrl(String referUrl) {
        this.referUrl = referUrl;
    }

    public String getReferUrlNoParams() {
        return referUrlNoParams;
    }

    public void setReferUrlNoParams(String referUrlNoParams) {
        this.referUrlNoParams = referUrlNoParams;
    }

    public String getReferUrlHost() {
        return referUrlHost;
    }

    public void setReferUrlHost(String referUrlHost) {
        this.referUrlHost = referUrlHost;
    }

    public String getReferPvId() {
        return referPvId;
    }

    public void setReferPvId(String referPvId) {
        this.referPvId = referPvId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
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

    public String getSpmCntA() {
        return spmCntA;
    }

    public void setSpmCntA(String spmCntA) {
        this.spmCntA = spmCntA;
    }

    public String getSpmCntB() {
        return spmCntB;
    }

    public void setSpmCntB(String spmCntB) {
        this.spmCntB = spmCntB;
    }

    public String getSpmPreA() {
        return spmPreA;
    }

    public void setSpmPreA(String spmPreA) {
        this.spmPreA = spmPreA;
    }

    public String getSpmPreB() {
        return spmPreB;
    }

    public void setSpmPreB(String spmPreB) {
        this.spmPreB = spmPreB;
    }

    public String getSpmPreC() {
        return spmPreC;
    }

    public void setSpmPreC(String spmPreC) {
        this.spmPreC = spmPreC;
    }

    public String getSpmPreD() {
        return spmPreD;
    }

    public void setSpmPreD(String spmPreD) {
        this.spmPreD = spmPreD;
    }

    public String getScmCntA() {
        return scmCntA;
    }

    public void setScmCntA(String scmCntA) {
        this.scmCntA = scmCntA;
    }

    public String getScmCntB() {
        return scmCntB;
    }

    public void setScmCntB(String scmCntB) {
        this.scmCntB = scmCntB;
    }

    public String getScmCntC() {
        return scmCntC;
    }

    public void setScmCntC(String scmCntC) {
        this.scmCntC = scmCntC;
    }

    public String getScmCntD() {
        return scmCntD;
    }

    public void setScmCntD(String scmCntD) {
        this.scmCntD = scmCntD;
    }

    public Integer getIsCrawler() {
        return isCrawler;
    }

    public void setIsCrawler(Integer isCrawler) {
        this.isCrawler = isCrawler;
    }

    public Double getIsAbnormal() {
        return isAbnormal;
    }

    public void setIsAbnormal(Double isAbnormal) {
        this.isAbnormal = isAbnormal;
    }

    public String getAbnormalRule() {
        return abnormalRule;
    }

    public void setAbnormalRule(String abnormalRule) {
        this.abnormalRule = abnormalRule;
    }

    public String getVstUserAgent() {
        return vstUserAgent;
    }

    public void setVstUserAgent(String vstUserAgent) {
        this.vstUserAgent = vstUserAgent;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Integer getIsNewUser() {
        return isNewUser;
    }

    public void setIsNewUser(Integer isNewUser) {
        this.isNewUser = isNewUser;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}


