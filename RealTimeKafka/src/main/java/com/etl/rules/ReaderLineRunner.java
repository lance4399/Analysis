package com.etl.rules;


/**
 *
 * 文件内容处理接口
 *
 * yqren
 *
 */
@FunctionalInterface
public interface ReaderLineRunner {
    void run(String line);
}
