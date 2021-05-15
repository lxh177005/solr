package com.xingxin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLog {
    private static Logger logger = LoggerFactory.getLogger(TestLog.class);
    public static void main(String[] args) {
        logger.debug("我是debug信息");
        logger.info("我是info信息");
        logger.warn("我是warn信息");
        logger.error("我是error信息");
    }
}
