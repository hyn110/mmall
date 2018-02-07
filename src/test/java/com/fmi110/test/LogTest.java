package com.fmi110.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author fmi110
 * @Description:
 * @Date 2018/2/7 21:05
 */
@Slf4j
public class LogTest {
    @Test
    public void testLog(){
        log.error("this is error log");
        log.info("this is info log");
    }
}
