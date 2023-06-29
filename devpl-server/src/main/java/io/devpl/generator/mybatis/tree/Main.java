package io.devpl.generator.mybatis.tree;

import com.alibaba.fastjson2.JSON;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        test2();

    }

    public static void test2() {
        TreeNode<String> forest = new TreeNode<>("root");
        TreeNode<String> current = forest;
        for (String tree : Arrays.asList("user.name", "user.hooby.1")) {
            TreeNode<String> root = current;
            for (String data : tree.split("\\.")) {
                current = current.addChild(data);
            }
            current = root;
        }

        System.out.println(JSON.toJSONString(forest));
    }
}
