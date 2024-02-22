package io.devpl.backend.mock;

import io.devpl.backend.entity.FieldGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 一个数据模拟库
 * <a href="https://github.com/jsonzou/jmockdata">jmockdata</a>
 */
public class Mocker {

    public static void main(String[] args) {

        int[] ints = Mocker.split(7, 232);

        FieldGroup group = new FieldGroup();

    }

    public static int[] split(int n, int sum) {
        return split(n, sum, false);
    }

    /**
     * 将整数sum拆分成n个正整数之和
     *
     * @param n    拆分成n个数
     * @param sum  和
     * @param flag 生成的正整数集合中是否允许为0
     * @return 拆分结果
     */
    public static int[] split(int n, int sum, boolean flag) {
        //随机抽取n-1个小于sum的数
        List<Integer> list = new ArrayList<>();
        //将0和sum加入到里list中
        list.add(0);
        //判断生成的正整数集合中是否允许为0，true元素可以为0  false元素不可以为0
        if (!flag) {
            sum = sum - n;
        }
        list.add(sum);
        int temp;
        for (int i = 0; i < n - 1; i++) {
            temp = (int) (Math.random() * sum);
            list.add(temp);
        }
        Collections.sort(list);
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = list.get(i + 1) - list.get(i);
            if (!flag) {
                nums[i] += 1;
            }
        }
        return nums;
    }

}
