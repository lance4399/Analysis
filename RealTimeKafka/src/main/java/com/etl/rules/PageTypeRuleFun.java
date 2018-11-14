package com.etl.rules;
import org.apache.commons.lang3.StringUtils;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static com.etl.rules.CommonTools.readResourceFile;

/**
 * PC Wap 页面类型规则
 */
public class PageTypeRuleFun {

    private static PageTypeRuleFun pageTypeRule = PageTypeRuleFun.getPageTypeRule();

    //一级页面规则文件
    private static String page_type_rule = "/page_type_rule";

    //二级页面规则文件
    private static String page_sub_type_rule = "/page_sub_type_rule";

    private static List<PTRule> page_type_ruleList = new ArrayList<>();

    private static List<SPTRule> page_sub_type_ruleList = new ArrayList<>();

    private PageTypeRuleFun() {}

    public static PageTypeRuleFun getPageTypeRule() {
        readResourceFile(page_type_rule,line -> page_type_ruleList.add(new PTRule(line)));
        readResourceFile(page_sub_type_rule,line -> page_sub_type_ruleList.add(new SPTRule(line)));
        return pageTypeRule;
    }

    //一级页面类型
    private static class PTRule {
        private String page_type_id;
        private String page_type;
        private String [] wapRules;
        private String [] pcRules;
        private String [] popRules;
        public String getPage_type_id() {return page_type_id;}
        public String getPage_type() {return page_type;}
        private Map<String,String[]> rulesMap = new HashMap<>(3);

        PTRule(String rule) {
            String[] item = rule.split("\\,");
            page_type_id = item[0];
            page_type = item[1];
            wapRules = item[2].split("#");
            pcRules = item[3].split("#");
            rulesMap.put("wap",wapRules);
            rulesMap.put("pc",pcRules);
            if(item.length == 5) {
                popRules=item[4].split("#");
                rulesMap.put("popup",popRules);
            }
        }

        boolean match(String host,String path,String business) {
            String[] rules = rulesMap.get(business);
            if(rules == null) return false;
            for(String rule : rules) {
                String [] res = rule.split(";");
                if(host.matches(res[0].split("=")[1]) && path.matches(res[1].split("=")[1])) return true;
            }
            return false;
        }
    }

    // 二级页面规则
    private static class SPTRule {

        private String page_sub_type_id;
        private String page_sub_type;
        private String page_type_id;
        private String page_type;
        private String business;
        private String[] rules;
        public String getPage_type_id() { return page_type_id; }
        public String getPage_type() { return page_type; }
        public String getPage_sub_type_id() { return page_sub_type_id; }
        public String getPage_sub_type() { return page_sub_type; }

        SPTRule(String rule) {
            String[] items = rule.split(",");
            page_sub_type_id=items[0];
            page_sub_type=items[1];
            page_type_id=items[3];
            page_type=items[4];
            business = items[2];
            rules = items[5].split("#");
        }

        boolean match(String host,String path,String business) {
            if(this.business.equals(business)) {
                for (String rule : rules) {
                    String [] res = rule.split(";");
                    if(host.matches(res[0].split("=")[1]) && path.matches(res[1].split("=")[1])) return true;
                }
            }
            return false;
        }
    }

    private static String def_page_type_id = "99";
    private static String def_page_type = "站外";
    private static String def_sub_page_type_id = "-1";
    private static String def_sub_page_type = "";

    public String[] matchPageType(URI uri,String business) {
        return matchPageType(uri.getHost(),uri.getPath(),business);
    }

    private String[] matchPageType(String host,String path,String business) {
        String [] returnCodes = new String [] {def_page_type_id,def_page_type,def_sub_page_type_id,def_sub_page_type};
        if(StringUtils.isBlank(host)){
            returnCodes[0] = "98";
            returnCodes[1] = "空白页面";
            returnCodes[2] = "-1";
            returnCodes[3] = "";
            return returnCodes;
        }

            //二级页面类型通用规则
            final Boolean[] matchSubFlag = {false};
            page_sub_type_ruleList.stream().forEach(sptRule -> {
                if (sptRule.match(host, path, business)) {
                    matchSubFlag[0]=true;
                    returnCodes[0] = sptRule.getPage_type_id();
                    returnCodes[1] = sptRule.getPage_type();
                    returnCodes[2] = sptRule.getPage_sub_type_id();
                    returnCodes[3] = sptRule.getPage_sub_type();
                }
            });
            if(!matchSubFlag[0]){
                returnCodes[2] = "-1";
                returnCodes[3] = "";
                //一级页面类型通用规则
                page_type_ruleList.stream().forEach(ptRule -> {
                    if (ptRule.match(host, path, business)) {
                        returnCodes[0] = ptRule.getPage_type_id();
                        returnCodes[1] = ptRule.getPage_type();
                    }
                });
            }

            if (returnCodes[0].equals("99") && (host.contains("asd.cn") || host.contains("5632.com")
                    || host.contains("s.com")
                    || host.contains("17173.com")
                    || host.contains("s.com")
                    || host.contains("so.com")
                    || host.contains("s.net")
                    || host.contains("ss.com")
                    || host.contains("sohupay.com")
                    || host.contains("m.x")
                    || host.contains("x.news.com")
                    )) {
                returnCodes[0] = "97";
                returnCodes[1] = "其他站";
                returnCodes[2] = "-1";
                returnCodes[3] = "";
            }
            return returnCodes;
    }

    private static void println(String[] arg) {
        System.out.println(arg[0] + " " + arg[1] + " " + arg[2] + " " + arg[3]);
    }

}