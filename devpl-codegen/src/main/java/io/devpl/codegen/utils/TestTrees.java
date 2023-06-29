package io.devpl.codegen.utils;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TestTrees {

    public static void main(String[] args) {
        List<TaskVersionLog> allList = new ArrayList<>();
        int depth = 0;
        List<TaskVersionLog> list = getLog("A", null, 3);
        while (true) {
            allList.addAll(list);
            if (depth == 8) {
                break;
            }
            List<TaskVersionLog> temp = new ArrayList<>();
            for (TaskVersionLog taskVersionLog : list) {
                temp.addAll(getLog(taskVersionLog.getId(), taskVersionLog.getId(), 3));
            }
            list = temp;
            depth++;
        }
        Collections.reverse(allList);

        long start = System.currentTimeMillis();
        TreeUtil<TaskVersionLog, String> treeUtility = TreeUtil.getInstance(TaskVersionLog::getSuperId, TaskVersionLog::getId, TaskVersionLog::setList);
        treeUtility.setComparator(Comparator.comparing(TaskVersionLog::getId));
        treeUtility.setNewInstance(TaskVersionLog::new);
        treeUtility.setOperate(node -> node.setName(node.getSuperId() + "_" + 2));
        treeUtility.init(allList);

        // 获取查询到的第一个节点
        TaskVersionLog firstNodes = treeUtility.getFirstNodes("A-1-1-1-1");
        System.out.println(firstNodes);
        // 获取指定节点的所有下级节点
        List<TaskVersionLog> downNodes = treeUtility.getDownNodes("A-1-1-1-1");
        System.out.println(downNodes.size());
        // 获取
        List<TaskVersionLog> rootNode = treeUtility.getRootNode("A-1-1-1-1");
        System.out.println(rootNode.size());
        TaskVersionLog superNode = treeUtility.getSuperNode("A-1-1-1-1");
        System.out.println(superNode);
        int level = treeUtility.getDepth("A-1-1-1-1");
        System.out.println(level);

        treeUtility.setFilter(node -> node.getNoFilterLevel() < 3);
        // 获取完整的树
        List<TaskVersionLog> tree = treeUtility.getTree();
        System.out.println(tree.size());

        long end = System.currentTimeMillis();
        System.out.println("总时间：" + (end - start));
    }

    @Data
    static class Tree {
        /**
         * id
         */
        private String id;

        /**
         * 名称
         */
        private String name;

        /**
         * 上级id
         */
        private String superId;

        /**
         * 下级数据
         */
        private List<Tree> children;

    }

    public static List<TaskVersionLog> getLog(String id, String superId, int row) {
        List<TaskVersionLog> list = new ArrayList<>();
        for (int index = 1; index <= row; index++) {
            TaskVersionLog v1 = new TaskVersionLog();
            v1.setId(id + "-" + index);
            v1.setSuperId(superId);
            list.add(v1);
        }
        return list;
    }

}

