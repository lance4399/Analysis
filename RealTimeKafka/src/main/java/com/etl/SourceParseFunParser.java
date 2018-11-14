package com.etl;

import com.etl.rules.PageTypeRuleFun;
import com.etl.rules.SourceRuleFun;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜狐页面来源类型解析（增加UA规则），返回四级来源类型ID
 * 输出结果类型：array
 * 输出结果：[页面来源ID,页面一级来源ID,页面二级来源ID,页面三级来源ID,页面四级来源ID]
 * */

public class SourceParseFunParser {
    private SourceRuleFun sourceRuleFun = null;
    private PageTypeRuleFun pageTypeRule = null;

    public SourceParseFunParser(){
        sourceRuleFun = SourceRuleFun.getSourceRuleFun();
        pageTypeRule = PageTypeRuleFun.getPageTypeRule();
    }
    public List<String> run(String page_url, String refer_url, String ua, String business){
        // /初始化规则器

        List<String> resultArr = new ArrayList<>();
        if(StringUtils.isNotBlank(page_url)) {
            try {
                String[] sourceArr = sourceRuleFun.matchSource(page_url,refer_url,ua,business);
                if(sourceArr[0].equals("-1")){
                    URI uriReferUrl = new URI(refer_url);
                    String[] pageTypeList =  pageTypeRule.matchPageType(uriReferUrl, business);
                    if(pageTypeList[0].equals("2")||pageTypeList[0].equals("3")||pageTypeList[0].equals("4")){
                        if (pageTypeList[2].equals("-1")){
                            resultArr.add("4"+pageTypeList[0]+"90");
                            resultArr.add("0");
                            resultArr.add("90"+pageTypeList[0]);
                            resultArr.add("90"+pageTypeList[0]+"90");
                            resultArr.add("-1");
                        }else{
                            resultArr.add("4"+pageTypeList[0]+pageTypeList[2]);
                            resultArr.add("0");
                            resultArr.add("90"+pageTypeList[0]);
                            resultArr.add("90"+pageTypeList[0]+pageTypeList[2]);
                            resultArr.add("-1");
                        }
                    } else if(pageTypeList[0].equals("97")){
                        //其他搜狐站：搜狐视频等
                        resultArr.add("2");
                        resultArr.add("0");
                        resultArr.add("0");
                        resultArr.add("0");
                        resultArr.add("-1");
                    } else if(pageTypeList[0].equals("99")){
                        //站外页面
                        resultArr.add("1");
                        resultArr.add("-1");
                        resultArr.add("-1");
                        resultArr.add("-1");
                        resultArr.add("-1");
                    } else{
                        resultArr.add("4"+pageTypeList[0]+pageTypeList[0]);
                        resultArr.add("0");
                        resultArr.add("90"+pageTypeList[0]);
                        resultArr.add("90"+pageTypeList[0]);
                        resultArr.add("-1");
                    }
                } else{
                    for(String p : sourceArr) {
                        resultArr.add(p);
                    }
                }
            } catch (Exception e) {
                resultArr.add("-1");
                resultArr.add("-1");
                resultArr.add("-1");
                resultArr.add("-1");
                resultArr.add("-1");
            }
        }
        return resultArr;
    }

    public String getSourceId(String page_url, String refer_url, String ua, String business){
        return run(page_url, refer_url, ua, business).get(0);
    }
    public String getSourceFirstId(String page_url, String refer_url, String ua, String business){
        return run(page_url, refer_url, ua, business).get(1);
    }
    public String getSourceSecondId(String page_url, String refer_url, String ua, String business){
        return run(page_url, refer_url, ua, business).get(2);

    }
    public String getSourceThirdId(String page_url, String refer_url, String ua, String business){
        return run(page_url, refer_url, ua, business).get(3);
    }
    public String getSourceFourthId(String page_url, String refer_url, String ua, String business){
        return run(page_url, refer_url, ua, business).get(4);
    }


}
