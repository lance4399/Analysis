package com.etl.rules;

import java.util.Scanner;


/**
 *
 * 通用处理工具
 *
 * yqren
 */
public class CommonTools {

    public static void readResourceFile(String path, ReaderLineRunner readerLine) {
         Scanner scanner = new Scanner(CommonTools.class.getResourceAsStream(path),"UTF-8");
         while (scanner.hasNext()) {
               readerLine.run(scanner.nextLine());
         }
         scanner.close();
    }

    public static String null2Blank(String str) {
        return str == null? "" : str;
    }
}
