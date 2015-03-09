package com.leilei.refresh.utils;

import java.util.Random;

/**
 * USER: liulei
 * DATE: 2015/3/9.
 * TIME: 13:51
 */
public class RandomUtils {

    private static Random random;
    private static boolean initialized = false;

    private RandomUtils() {
    }

    /**
     * @param start 开始
     * @param end   结束
     * @return 构造[start, end)之间的随机数
     */
    public static int betweentInt(int start, int end) {
        init();
        if (end < start)
            throw new IllegalArgumentException("end must be larger than start.");
        return start + random.nextInt(end - start);
    }

    private static void init() {
        if (!initialized) {
            random = new Random();
            initialized = true;
        }
    }
}
