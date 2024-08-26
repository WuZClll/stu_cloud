package com.xi.cloud;

import java.time.ZonedDateTime;

/**
 * @author ZC_Wu 汐
 * @date 2024/8/9 16:40:04
 * @description
 */
public class Main {
    public static void main(String[] args) {
        ZonedDateTime zbj = ZonedDateTime.now(); // 默认时区
        System.out.println(zbj);
    }
}