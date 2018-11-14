package com.etl;
import com.etl.rules.PageTypeRuleFun;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析页面类型
 *
 * 返回值 数组 [页面一级类型id,页面一级类型名称,页面二级类型id,页面二级类型名称]
 */
public class PageTypeParseParser {
    private PageTypeRuleFun pageTypeRule;
    public PageTypeParseParser(){
        // /初始化规则器
        this.pageTypeRule = PageTypeRuleFun.getPageTypeRule();
    }

    public List<String> run(String page_url, String business) {
        List<String> resultArr = new ArrayList<>();
        if(StringUtils.isNotBlank(page_url) && !("-").equals(page_url)) {
            try {
//                System.out.println("[page_url is not blank ]");
                URI uri = new URI(page_url.trim());
                String[] pageTypeArr = pageTypeRule.matchPageType(uri,business);
                for(String p : pageTypeArr) {
                    resultArr.add(p);
                }
            } catch (Exception e) {
                resultArr.add("98");
                resultArr.add("空白页面");
                resultArr.add("-1");
                resultArr.add("");
            }
        } else {
//            System.out.println("[page_url is null or -]");
            resultArr.add("98");
            resultArr.add("空白页面");
            resultArr.add("-1");
            resultArr.add("");
        }
        return resultArr;
    }

    //返回值 数组 [页面一级类型id,页面一级类型名称,页面二级类型id,页面二级类型名称]
    public String getPageTypeId(String page_url, String business){
        return run(page_url, business).get(0);
    }

    public String getPageType(String page_url,String business){
        return run(page_url, business).get(1);
    }

    public String getPageSubTypeId(String page_url, String business){
        return run(page_url, business).get(2);
    }

    public String getPageSubType(String page_url, String business){
        return run(page_url, business).get(3);
    }
}
