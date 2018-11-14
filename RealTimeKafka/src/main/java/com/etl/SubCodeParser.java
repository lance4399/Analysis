package com.etl;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 解析 SPM 码或 SCM 码
 *
 * 参数 SubCodeParser(spm,'b','1')
 * 参数1：spm码或scm码
 * 参数2：要获取的码的位置 a,b,c,d。没法获取e位置
 * 参数3：1：该位置的标准码（不包含A/B Test 版本号）
 *       2：该位置全码（会包含有 A/B Test 版本号）
 *       3：该位置的A/BTest版本号
 *
 */
public class SubCodeParser {

    private static Map<String,Integer> posMap = new HashMap<>();
    static {
        posMap.put("a",0);
        posMap.put("b",1);
        posMap.put("c",2);
        posMap.put("d",3);
        posMap.put("e",4);
    }

    public String run(String code, String pos) {
        String result = "";
        if(code == null) return result;
        Integer idx = 0;
        String[] items = code.trim().split("\\.");
        if(StringUtils.isNotBlank(code.trim())
                && pos != null
                && items.length >= 4 )
        {
            String[] posItem = pos.split("\\.");
            if (posItem.length <= 1) {
                // 第一种模式，按照位置取码
                if((idx = posMap.get(pos.toLowerCase())) != null) {
                    //如果只传前两个参数，默认获得全码
                    result = items[idx];
                } else {
                    throw new RuntimeException("The parameters should be 'a' or 'b' or 'c' or 'd' or 'e'!");
                }
            } else {
                // 模式2，第二个参数可以是 "a.b.c" 这种模式
                result = getString(pos, result, items, posItem);
            }
        }
        return result;
    }

    private String getString(String pos, String result, String[] items, String[] posItem) {
        if(pos.matches("a\\.b(\\.c(\\.d)?)?")) {
            int size = posItem.length > 4 ? 4 : posItem.length;
            StringBuilder sb = new StringBuilder();
            for(int i=0 ; i < size ; i++) {
                if(i > 0) sb.append(".");
                sb.append(items[i]);
            }
            result = sb.toString();
        }
        return result;
    }

    public String run(String code, String pos, String type) {
        String result = "";
        if(code == null) return result;
        Integer idx = 0;
        String[] items = code.trim().split("\\.");
        if(StringUtils.isNotBlank(code.trim())
                && pos != null
                && items.length >= 4 )
        {
            String[] posItem = pos.split("\\.");
            if (posItem.length <= 1) {
                // 第一种模式，按照位置取码
                if((idx = posMap.get(pos.toLowerCase())) != null) {
                    String subCode = items[idx];
                    String[] subItems = subCode.split("/");
                    if(("1").equals(type)) {
                        result = subItems[0];
                    } else if (("2").equals(type)) {
                        result = subCode;
                    } else if (("3").equals(type) && subItems.length == 2) {
                        result = subItems[1];
                    }
                } else {
                    throw new RuntimeException("The parameters should be 'a' or 'b' or 'c' or 'd' or 'e'!");
                }
            } else {
                // 模式2，第二个参数可以是 "a.b.c" 这种模式
                result = getString(pos, result, items, posItem);
            }
        }
        return result;
    }
}
