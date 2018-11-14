package com.etl.rules;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.etl.rules.CommonTools.readResourceFile;

/**
 * 4级来源类型ID规则
 * 输入:page_url,refer_url,business
 * 输出数组[来源ID，1级来源ID，2级来源ID，3级来源ID，4级来源ID]
 */
public class SourceRuleFun {
    private static SourceRuleFun sourceRuleFun = SourceRuleFun.getSourceRuleFun();
    //page_url规则文件
    private static String source_page_rule = "/source_page_rule";
    //refer_url规则文件
    private static String source_refer_rule = "/source_refer_rule";
    //UA规则文件
    private static String source_ua_rule = "/source_ua_rule";
    
    private static List<SRule> source_page_ruleList = new ArrayList<>();

    private static List<SRule> source_refer_ruleList = new ArrayList<>();
    private static List<SRule> source_ua_ruleList = new ArrayList<>();

    private SourceRuleFun() {}

    public static SourceRuleFun getSourceRuleFun() {
        readResourceFile(source_page_rule,line -> source_page_ruleList.add(new SRule(line)));
        readResourceFile(source_refer_rule,line -> source_refer_ruleList.add(new SRule(line)));
        readResourceFile(source_ua_rule,line -> source_ua_ruleList.add(new SRule(line)));
        return sourceRuleFun;
    }

    private static class SRule {
        public String getSource_id() {return source_id;}
        public String getSource_first_id() {return source_first_id;}
        public String getSource_second_id() {return source_second_id;}
        public String getSource_third_id() {return source_third_id;}
        public String getSource_fourth_id() {return source_fourth_id;}

        private String source_id;
        private String source_first_id;
        private String source_second_id;
        private String source_third_id;
        private String source_fourth_id;
        private String rules;

        private Map<String,String> rulesMap = new HashMap<>(1);

        SRule(String rule) {
            String[] item = rule.split("\\,");
            source_id = item[0];
            source_first_id = item[1];
            source_second_id = item[2];
            source_third_id = item[3];
            source_fourth_id = item[4];
            rules = item[5];
            rulesMap.put("rule",rules);
        }

        boolean pageUrlMatch(String page_url) {
            String rules = rulesMap.get("rule");
            return page_url.matches(rules);
        }

        boolean referUrlMatch(String refer_url){
            String rule = rulesMap.get("rule");
            return refer_url.matches(rule);
        }

        boolean uaMatch(String ua){
            String rule = rulesMap.get("rule");
            return ua.matches(rule);
        }
    }

    private boolean isLegalUrl(String url){
        try{
            URI uriObj = new URI(url);
            URL urlObj = new URL(url);
            return url.length()>9;
        }catch (Exception e) {
            return false;
        }
    }

    private static String def_source_id = "1";
    private static String def_first_id = "-1";
    private static String def_second_id = "-1";
    private static String def_third_id= "-1";
    private static String def_fourth_id = "-1";

    public String[] matchSource(String pageUrl,String referUrl,String ua,String business) {
        String[] returnCodes = new String[]{def_source_id, def_first_id, def_second_id, def_third_id, def_fourth_id};
        final Boolean[] matchFlag = {false, false, false};
        //处理UA识别社交回流数据：
        String ua_lower=ua.toLowerCase();
        source_ua_ruleList.stream().forEach(sRule -> {
            if (sRule.uaMatch(ua_lower)) {
                matchFlag[2] = true;
                returnCodes[0] = sRule.getSource_id();
                returnCodes[1] = sRule.getSource_first_id();
                returnCodes[2] = sRule.getSource_second_id();
                returnCodes[3] = sRule.getSource_third_id();
                returnCodes[4] = sRule.getSource_fourth_id();
            }
        });

        //处理page_url合法的数据
        if (!matchFlag[2]) {
            if (isLegalUrl(pageUrl)) {
                //匹配page_url
                source_page_ruleList.stream().forEach(sRule -> {
                            if (sRule.rulesMap.get("rule") != null) {
                                if (sRule.pageUrlMatch(pageUrl)) {
                                    matchFlag[0] = true;
                                    returnCodes[0] = sRule.getSource_id();
                                    returnCodes[1] = sRule.getSource_first_id();
                                    returnCodes[2] = sRule.getSource_second_id();
                                    returnCodes[3] = sRule.getSource_third_id();
                                    returnCodes[4] = sRule.getSource_fourth_id();
                                }
                            }
                        }
                );
                //匹配refer合法的数据
                source_refer_ruleList.stream().forEach(sRule -> {
                    if (sRule.rulesMap.get("rule") != null) {
                        boolean isLegalRefer = isLegalUrl(referUrl);
                        if (isLegalRefer && !matchFlag[0]) {
                            if (sRule.referUrlMatch(referUrl)) {
                                matchFlag[1] = true;
                                returnCodes[0] = sRule.getSource_id();
                                returnCodes[1] = sRule.getSource_first_id();
                                returnCodes[2] = sRule.getSource_second_id();
                                returnCodes[3] = sRule.getSource_third_id();
                                returnCodes[4] = sRule.getSource_fourth_id();
                            }
                        } else if (isLegalRefer && returnCodes[2].equals("37")) {
                            if (sRule.referUrlMatch(referUrl)) {
                                matchFlag[1] = true;
                                returnCodes[0] = sRule.getSource_id();
                                returnCodes[1] = sRule.getSource_first_id();
                                returnCodes[2] = sRule.getSource_second_id();
                                returnCodes[3] = sRule.getSource_third_id();
                                returnCodes[4] = sRule.getSource_fourth_id();
                            }
                        }
                    }
                });
                //处理page_url和refer_url均未能匹配的数据,处理UA识别社交回流数据：
                if (!matchFlag[0] && !matchFlag[1]) {
                    source_ua_ruleList.stream().forEach(sRule -> {
                        if (sRule.uaMatch(ua)) {
                            matchFlag[0] = true;
                            returnCodes[0] = sRule.getSource_id();
                            returnCodes[1] = sRule.getSource_first_id();
                            returnCodes[2] = sRule.getSource_second_id();
                            returnCodes[3] = sRule.getSource_third_id();
                            returnCodes[4] = sRule.getSource_fourth_id();
                        }
                    });
                }
                //处理page_url和refer_url以及UA均未匹配到且refer_url不合法的数据：直接访问
                if (!matchFlag[0] && !matchFlag[1]) {
                    if (!isLegalUrl(referUrl)) {
                        matchFlag[0] = true;
                        returnCodes[0] = "76";
                        returnCodes[1] = "7";
                        returnCodes[2] = "1";
                        returnCodes[3] = "1";
                        returnCodes[4] = "-1";
                    } else {
                        // referUrl合法且未匹配到渠道的数据，在SourceParse中进一步进行referUrl的页面类型判断
                        matchFlag[0] = true;
                        returnCodes[0] = "-1";
                        returnCodes[1] = "-1";
                        returnCodes[2] = "-1";
                        returnCodes[3] = "-1";
                        returnCodes[4] = "-1";
                    }
                }
            } else {
                //pageUrl不合法:其他—>空白页
                matchFlag[0] = true;
                returnCodes[0] = "320";
                returnCodes[1] = "6";
                returnCodes[2] = "99";
                returnCodes[3] = "991";
                returnCodes[4] = "-1";
            }
        }
        return returnCodes;
    }

    private static void println(String[] arg) {
        System.out.println(arg[0] + " " + arg[1] + " " + arg[2] + " " + arg[3]+ " " + arg[4]);
    }


}