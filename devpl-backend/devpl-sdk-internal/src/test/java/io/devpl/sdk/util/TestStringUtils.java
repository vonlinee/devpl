package io.devpl.sdk.util;

import io.devpl.sdk.DataClass;
import io.devpl.sdk.DataObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class TestStringUtils {

    @Test
    public void test1() {

        System.out.println(StringUtils.isBlank("   s    "));

        StringBuilder sb = new StringBuilder();

        sb.append("Hello World");

        String result = StringUtils.lastSubstring(sb, 0);

        System.out.println(result);

        DataObject obj = DataClass.newObject();

        obj.set("name", "zs");
        obj.set("age", 12);
    }

    @Test
    public void test2() {
        String str1 = StringUtils.joinWithSeparator("A", "B");
        String str2 = StringUtils.joinWithSeparator("A", "B", "C", "D");

        Assertions.assertEquals(str1, "B");
        Assertions.assertEquals(str2, "BACAD");
    }

    public static String generateRandomString(int length) {
        // 定义字符集
        String charSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // 从字符集中随机选择一个字符
            int randomIndex = random.nextInt(charSet.length());
            char randomChar = charSet.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        // 生成一个长度为10的随机字符串
        String randomString = generateRandomString(10);
        System.out.println(randomString);

        testCalculateSimilarity();
    }

    public static void testCalculateSimilarity() {

        String s = generateRandomString(10);

        String s1 = StringUtils.insertAt(s, 4, "ab");

        float v = StringUtils.calculateSimilarity(s, s1);

        System.out.println(v);
    }
}
