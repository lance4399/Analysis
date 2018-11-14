package com.handlers.ods2dwd;

import com.entity.dwd.DwdShmmWebPvDi;
import com.entity.ods.OdsShmmWebPvDi;
import com.enums.TranslatorLevel;
import com.etl.*;
import com.handlers.Handler;
import com.models.dwd.DwdShmmWebPvDiModel;
import com.models.ods.OdsShmmWebPvDiModel;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: xiliang
 * @Date: 2018/8/14 11:06
 **/

public class Ods2Dwd_WebPvDiHandler  extends Handler<OdsShmmWebPvDiModel, DwdShmmWebPvDiModel> {

    private static Ods2Dwd_WebPvDiHandler odsShmmWebPvDiHandler;

    private Ods2Dwd_WebPvDiHandler(){}
    public static Ods2Dwd_WebPvDiHandler getOdsShmmWebPvDiHandler(){
        if (odsShmmWebPvDiHandler==null){
            synchronized (Ods2Dwd_WebPvDiHandler.class){
                if(odsShmmWebPvDiHandler == null)
                    odsShmmWebPvDiHandler = new Ods2Dwd_WebPvDiHandler();
            }
        }
        return odsShmmWebPvDiHandler;
    }

    GetPvIdParser getPvIdParser = new GetPvIdParser();
    UrlParser urlParser = new UrlParser();
    TakeHttpParser takeHttpParser = new TakeHttpParser();
    SourceParseFunParser sourceParseFunParser = new SourceParseFunParser();
    SubCodeParser subCodeParser = new SubCodeParser();
    CommonParser commonParser = new CommonParser();
    PageTypeParseParser pageTypeParseParser = new PageTypeParseParser();
    private final String regex = "spider|bot|slurp|python|^java|httpclient|^wget|^ruby|isa.*server.*connectivity.*check|^jakarta";
    private final Pattern pattern = Pattern.compile(regex);

    @Override
    public DwdShmmWebPvDiModel parsing(OdsShmmWebPvDiModel input) {
        OdsShmmWebPvDi odsShmmWebPvDi = input.getData();
        DwdShmmWebPvDi dwdShmmWebPvDi = new DwdShmmWebPvDi();
        dwdShmmWebPvDi.setLogVersion(objectToInt(odsShmmWebPvDi.getLogVersion()));
        dwdShmmWebPvDi.setLogTime(objectToLong(odsShmmWebPvDi.getLogTime()));
        dwdShmmWebPvDi.setLogTimeStamp(objectToLong(odsShmmWebPvDi.getLogTimeStamp()));
        dwdShmmWebPvDi.setJsVersion(odsShmmWebPvDi.getJsVersion());
        dwdShmmWebPvDi.setVstCookie(odsShmmWebPvDi.getVstCookie());

        String ua = odsShmmWebPvDi.getVstUserAgent();
        String pagePvId = getPvIdParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmCnt(), "GBK"));
        if (null != pagePvId)
            dwdShmmWebPvDi.setPagePvId(pagePvId);
        /**  page_url */
        String page_url_string = takeHttpParser.run((odsShmmWebPvDi.getPageUrl())); //去掉协议头
        StringBuffer page_sb = new StringBuffer();

        if (null != page_url_string) {
            dwdShmmWebPvDi.setPageUrl(page_url_string);
            String protocol = null;
            String page_url_host = null;
            String page_url_path = null;
            try {
                URL page_url = new URL(odsShmmWebPvDi.getPageUrl());
                protocol = page_url.getProtocol()+"://";
                page_url_host = page_url.getHost();
                page_url_path = page_url.getPath();
                dwdShmmWebPvDi.setPageUrlNoParams(page_url_host + page_url_path);
                dwdShmmWebPvDi.setPageUrlHost(page_url_host);
            } catch (IOException e) {
                System.out.println(("page_url:  '" + page_url_string + "' 解析出错！！！"));
            }
            page_sb.append(protocol).append(page_url_host).append(page_url_path);
            String concat_page_url ="http://"+page_url_host + page_url_path;
            dwdShmmWebPvDi.setPageTypeId(Integer.parseInt(pageTypeParseParser.getPageTypeId(concat_page_url, odsShmmWebPvDi.getBusiness())));
            dwdShmmWebPvDi.setPageType(pageTypeParseParser.getPageType(concat_page_url, odsShmmWebPvDi.getBusiness()));
        }
        StringBuffer page_refer_sb = new StringBuffer();
        String page_refer_rul_string = odsShmmWebPvDi.getPageReferUrl();
        if (!"-".equals(page_refer_rul_string) && null != page_refer_rul_string && !page_refer_rul_string.equals("null")) {
            dwdShmmWebPvDi.setReferUrl(takeHttpParser.run(odsShmmWebPvDi.getPageReferUrl()));
            String page_refer_rul_protocol;
            String page_refer_rul_host ;
            String page_refer_rul_path ;
            try {
                URL page_refer_rul = new URL(page_refer_rul_string);
                page_refer_rul_protocol = page_refer_rul.getProtocol()+"://";
                page_refer_rul_host = page_refer_rul.getHost();
                page_refer_rul_path = page_refer_rul.getPath();
                page_refer_sb.append(page_refer_rul_protocol).append(page_refer_rul_host).append(page_refer_rul_path);
                String referUrlNoParams;
                if(page_refer_sb.toString().endsWith("/"))
                    referUrlNoParams = page_refer_sb.toString().substring(0, page_refer_sb.toString().lastIndexOf("/"));
                else
                    referUrlNoParams = page_refer_sb.toString();
                dwdShmmWebPvDi.setReferUrlNoParams(referUrlNoParams);
                dwdShmmWebPvDi.setReferUrlHost(page_refer_rul_host);
                dwdShmmWebPvDi.setReferPvId(getPvIdParser.run(odsShmmWebPvDi.getSpmPre()));
                String page_url_lowerCase = page_sb.toString().toLowerCase();
                String page_refer_url_lowerCase = page_refer_sb.toString().toLowerCase();
                dwdShmmWebPvDi.setSourceId(sourceParseFunParser.getSourceId(page_url_lowerCase, page_refer_url_lowerCase, ua, odsShmmWebPvDi.getBusiness()));
            } catch (MalformedURLException e) {
                System.out.println(("refer_url:  '" + page_refer_rul_string + "' 解析出错！！！"));
            }
        }
        dwdShmmWebPvDi.setSpmCnt(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmCnt(), "GBK"), "a.b.c.d", "2"));
        dwdShmmWebPvDi.setSpmPre(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmPre(), "GBK"), "a.b.c.d", "2"));
        dwdShmmWebPvDi.setScmCnt(odsShmmWebPvDi.getScmCnt());
        dwdShmmWebPvDi.setSpmCntA(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmCnt(), "GBK"), "a", "1"));
        dwdShmmWebPvDi.setSpmCntB(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmCnt(), "GBK"), "b", "1"));
        dwdShmmWebPvDi.setSpmPreA(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmPre(), "GBK"), "a", "1"));
        dwdShmmWebPvDi.setSpmPreB(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmPre(), "GBK"), "b", "1"));
        dwdShmmWebPvDi.setSpmPreC(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmPre(), "GBK"), "c", "1"));
        dwdShmmWebPvDi.setSpmPreD(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getSpmPre(), "GBK"), "d", "1"));
        dwdShmmWebPvDi.setScmCntA(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getScmCnt(), "GBK"), "a", "1"));
        dwdShmmWebPvDi.setScmCntB(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getScmCnt(), "GBK"), "b", "1"));
        dwdShmmWebPvDi.setScmCntC(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getScmCnt(), "GBK"), "c", "1"));
        dwdShmmWebPvDi.setScmCntD(subCodeParser.run(urlParser.decoder(odsShmmWebPvDi.getScmCnt(), "GBK"), "d", "1"));
        int isCrawler = 0;
        Matcher matcher = pattern.matcher(ua.toLowerCase());
        if(matcher.find()){ isCrawler = 1; }
        dwdShmmWebPvDi.setIsCrawler(isCrawler);
        dwdShmmWebPvDi.setVstUserAgent(ua);
        dwdShmmWebPvDi.setExt(commonParser.mapToString(odsShmmWebPvDi.getExtMap()));
        int isNewUser = 0;
        Map<String, String> extMap = odsShmmWebPvDi.getExtMap();
        if (extMap.containsKey("isnewuser")){isNewUser = 1;}
        dwdShmmWebPvDi.setIsNewUser(isNewUser);
        dwdShmmWebPvDi.setBusiness(odsShmmWebPvDi.getBusiness());
        DwdShmmWebPvDiModel dwdShmmWebPvDiModel = new DwdShmmWebPvDiModel();
        dwdShmmWebPvDiModel.setData(dwdShmmWebPvDi);
//        logger.info("PvHandler完成");
        return dwdShmmWebPvDiModel;
    }

    @Override
    public TranslatorLevel getHandlerLevel() {
        return TranslatorLevel.OdsShmmWebPvDi;
    }

}

