package cn.arry.utils;

import cn.arry.type.ConstType;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具
 */
public class RandomUtil {
    public static int next(int maxValue) {
        return ThreadLocalRandom.current().nextInt(maxValue);
    }

    /**
     * 随机区间值
     * [minValue, maxValue)或[minValue, maxValue]
     */
    public static int next(int minValue, int maxValue) {
        return next(minValue, maxValue, false);
    }

    public static int next(int minValue, int maxValue, boolean isContainRight) {
        if (minValue < maxValue) {
            int bound;
            if (isContainRight) {
                bound = maxValue + 1 - minValue;
            } else {
                bound = maxValue - minValue;
            }

            return ThreadLocalRandom.current().nextInt(bound) + minValue;
        }

        return minValue;
    }

    public static double nextDouble(double maxValue) {
        return ThreadLocalRandom.current().nextDouble(maxValue);
    }

    /**
     * 检测概率
     */
    public static boolean checkProbability(int probability) {
        int probabilityRandom = next(ConstType.BasePercentIntNumber);
        return probabilityRandom <= probability;
    }

    /**
     * 随机一个boolean值
     */
    public static boolean randomBoolean() {
        return checkProbability(ConstType.BasePercentIntNumber / 2);
    }
}
