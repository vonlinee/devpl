package io.devpl.generator.mock;

import java.util.Random;

/**
 * Random 随机数生成器
 */
public class RandomHelper {

    private final Random random;

    public RandomHelper() {
        random = new Random();
    }

    public RandomHelper(long seed) {
        random = new Random(seed);
    }

    /**
     * 获取一个伪随机，在0(包括)和指定值(不包括)，从此随机数生成器的序列中取出均匀分布的int值。
     *
     * @param n        这是结合于该随机数返回。必须为正数。
     * @param negative 是否负数，必须是正数或负数
     * @return 在方法调用返回介于0(含)和n(不含)伪随机，均匀分布的int值。
     */
    public int randomInt(int n, boolean negative) {
        int i = random.nextInt(n);
        return negative ? -i : i;
    }

    /**
     * 会随机生成一个整数，这个整数的范围就是int类型的范围-2^31 ~ 2^31-1,但是如果在nextInt()括号中加入一个整数a那么，这个随机生成的随机数范围就变成[0,a)。
     *
     * @param negative 是否负数，必须是正数或负数
     * @return 随机生成一个整数
     */
    public int randomInt(boolean negative) {
        int res;
        while (true) {
            res = random.nextInt();
            if (negative && res < 0) {
                break;
            }
            if (!negative && res > 0) {
                break;
            }
        }
        return res;
    }

    public int randomInt(int a, int b) {
        int min = Math.min(a, b);
        int max = Math.max(a, b);
        return random.nextInt(max - min) + min;
    }

    public static void main(String[] args) {
        RandomHelper helper = new RandomHelper();

        for (int i = 0; i < 100; i++) {
            System.out.println(helper.randomInt(true));
        }
    }
}
