package com.tamsiree.rxkit.demodata.kit;

/**
 * <pre>
 * Luhn算法工具类
 * Created by Binary Wang on 2018/3/22.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class LuhnUtils {
    /**
     * 从不含校验位的银行卡卡号采用 Luhn 校验算法获得校验位
     * 该校验的过程：
     * 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    public static int getLuhnSum(char[] chs) {
        int luhnSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhnSum += k;
        }
        return luhnSum;
    }
}
