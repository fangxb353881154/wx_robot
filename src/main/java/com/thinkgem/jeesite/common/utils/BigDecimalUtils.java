package com.thinkgem.jeesite.common.utils;

import java.math.BigDecimal;

/**
 * Created by jfang on 2017/4/23.
 */
public class BigDecimalUtils {

    /**
     * 百分比
     * @param bigDecimal
     * @return
     */
    public static BigDecimal dividePoint(BigDecimal bigDecimal) {
        return bigDecimal.divide(new BigDecimal(100));
    }

    public static BigDecimal toBigDecimal(int i) {
        return new BigDecimal(i);
    }

    public static BigDecimal toBigDecimal(String s) {
        return new BigDecimal(s);
    }


}
