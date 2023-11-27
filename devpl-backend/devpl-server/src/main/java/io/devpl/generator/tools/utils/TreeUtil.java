package io.devpl.generator.tools.utils;

import java.util.*;
import java.util.function.*;

/**
 * 树形数据工具类
 */
public class TreeUtil<T, R> {

    /**
     * 上级数据主标识字段
     */
    private final Function<T, R> getSuperCode;
    /**
     * 数据主标识字段
     */
    private final Function<T, R> getCode;
    /**
     * 下级数据存放字段
     */
    private final BiConsumer<T, List<T>> children;
    private final List<TreeNode<T, R>> rootNodes = new ArrayList<>();
    /**
     * 对象创建方法
     */
    private Supplier<T> newInstance;
    /**
     * 数据排序方法
     */
    private Comparator<T> comparator;
    /**
     * 对数据的操作
     */
    private Consumer<T> operate;
    /**
     * 对数据的筛选
     */
    private Predicate<TreeNode<T, R>> filter;

    private TreeUtil(Function<T, R> getSuperCode, Function<T, R> getCode, BiConsumer<T, List<T>> children) {
        this.getSuperCode = getSuperCode;
        this.getCode = getCode;
        this.children = children;
    }

    /**
     * 获取工具类对象
     * @param getSuperCode 上级标识获取字段
     * @param getCode      当前数据主标识获取字段
     * @param children     下级数据存放字段
     * @param <T>          对象
     * @param <R>          标识类型
     * @return 树形数据处理工具
     */
    public static <T, R> TreeUtil<T, R> getInstance(Function<T, R> getSuperCode, Function<T, R> getCode, BiConsumer<T, List<T>> children) {
        return new TreeUtil<>(getSuperCode, getCode, children);
    }

    /**
     * 解除与原始数据的关联（需在初始化之前进行设置，初始化后设置无效）
     * @param newInstance 对象创建方法
     */
    public void setNewInstance(Supplier<T> newInstance) {
        this.newInstance = newInstance;
    }

    /**
     * 设置树形数据排序方法（需在初始化之前进行设置，初始化后设置无效）
     * @param comparator 排序方法
     */
    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /**
     * 设置对数据的操作（需在初始化之前进行设置，初始化后设置无效）
     * @param operate 获取数据时的操作
     */
    public void setOperate(Consumer<T> operate) {
        this.operate = operate;
    }

    /**
     * 设置对数据的过滤
     * @param filter 获取数据时的过滤
     */
    public void setFilter(Predicate<TreeNode<T, R>> filter) {
        this.filter = filter;
    }

    /**
     * 初始化工具
     * @param original 原始数据
     */
    public void init(List<T> original) {
        if (getSuperCode == null) {
            throw new NullPointerException("The parent identifier field is not set");
        }
        if (getCode == null) {
            throw new NullPointerException("The identity field is not set");
        }
        if (children == null) {
            throw new NullPointerException("The subordinate data receive field is not set");
        }
        if (original == null) {
            throw new NullPointerException("The original data is empty");
        }
        // 数据封装为节点对象
        Map<R, List<TreeNode<T, R>>> trNodes = new HashMap<>();
        Map<R, TreeNode<T, R>> nodes = new HashMap<>();
        original.forEach(data -> {
            if (data != null) {
                R code = getCode.apply(data);
                boolean containsCode = nodes.containsKey(code);
                if (containsCode) {
                    throw new IllegalArgumentException("The primary identity [" + code + "] is not a unique value");
                }
                R superCode = getSuperCode.apply(data);
                TreeNode<T, R> treeNode = new TreeNode<>(data, code, superCode, newInstance, operate);
                nodes.put(code, treeNode);
                List<TreeNode<T, R>> models = trNodes.computeIfAbsent(superCode, k -> new ArrayList<>());
                models.add(treeNode);
            }
        });
        if (nodes.isEmpty()) {
            return;
        }
        // 开始构树
        for (Map.Entry<R, List<TreeNode<T, R>>> entry : trNodes.entrySet()) {
            List<TreeNode<T, R>> treeNodes = entry.getValue();
            if (treeNodes == null || treeNodes.isEmpty()) {
                continue;
            }
            if (comparator != null) {
                treeNodes.sort((o1, o2) -> comparator.compare(o1.nodeData, o2.nodeData));
            }

            TreeNode<T, R> superNode = nodes.get(entry.getKey());
            if (superNode == null) {
                rootNodes.addAll(treeNodes);
                continue;
            }
            superNode.setNextNodes(treeNodes);
        }
    }

    /**
     * 获取树型数据
     * @return 树型集合
     */
    public List<T> getTree() {
        if (this.children == null) {
            throw new NullPointerException("The subordinate data receive field is not set");
        }
        if (rootNodes.isEmpty()) {
            return null;
        }
        List<T> trModels = new ArrayList<>();
        for (TreeNode<T, R> rootNode : rootNodes) {
            T nodeData = rootNode.getNodeModel(this.children, this.filter);
            if (nodeData != null) {
                trModels.add(nodeData);
            }
        }
        return trModels;
    }

    /**
     * 获取节点
     * @param code 数据主标识
     * @return 与数据主标识匹配的节点
     */
    public T getFirstNodes(R code) {
        if (this.children == null) {
            throw new NullPointerException("The subordinate data receive field is not set");
        }
        if (rootNodes.isEmpty()) {
            return null;
        }
        for (TreeNode<T, R> rootNode : rootNodes) {
            TreeNode<T, R> nextNode = rootNode.getNextNode(code, this.filter);
            if (nextNode != null) {
                return nextNode.getNodeModel(this.children, this.filter);
            }
        }
        return null;
    }

    /**
     * 获取节点深度
     * @param code 数据主标识
     * @return 与数据主标识匹配的节点深度(- 1表示未找到)
     */
    public int getDepth(R code) {
        if (rootNodes.isEmpty()) {
            return -1;
        }
        for (TreeNode<T, R> rootNode : rootNodes) {
            TreeNode<T, R> nextNode = rootNode.getNextNode(code, this.filter);
            if (nextNode != null) {
                return nextNode.getLevel(this.filter);
            }
        }
        return -1;
    }

    /**
     * 获取父级节点
     * @param code 数据主标识
     * @return 与数据主标识匹配节点的上级节点
     */
    public T getSuperNode(R code) {
        if (this.children == null) {
            throw new NullPointerException("The subordinate data receive field is not set");
        }
        if (rootNodes.isEmpty()) {
            return null;
        }
        for (TreeNode<T, R> rootNode : rootNodes) {
            TreeNode<T, R> nextNode = rootNode.getNextNode(code, this.filter);
            if (nextNode == null) {
                continue;
            }
            TreeNode<T, R> superNode = nextNode.superNode;
            if (superNode != null) {
                return superNode.getNodeModel(this.children, this.filter);
            }
        }
        return null;
    }

    /**
     * 获取根节点
     * @param code 数据主标识
     * @return 与数据主标识匹配的树型链的根节点
     */
    public List<T> getRootNode(R code) {
        if (this.children == null) {
            throw new NullPointerException("The subordinate data receive field is not set");
        }
        if (rootNodes.isEmpty()) {
            return null;
        }
        List<T> rootModels = new ArrayList<>();
        for (TreeNode<T, R> rootNode : rootNodes) {
            boolean hasNode = rootNode.hasNode(code, this.filter);
            if (hasNode) {
                rootModels.add(rootNode.getNodeModel(this.children, this.filter));
            }
        }
        return rootModels;
    }

    /**
     * 获取下级节点
     * @param code 数据主标识
     * @return 与数据主标识匹配的节点的下级节点
     */
    public List<T> getDownNodes(R code) {
        if (this.children == null) {
            throw new NullPointerException("The subordinate data receive field is not set");
        }
        if (rootNodes.isEmpty()) {
            return null;
        }
        List<T> trModels = new ArrayList<>();
        for (TreeNode<T, R> rootNode : rootNodes) {
            TreeNode<T, R> nextNode = rootNode.getNextNode(code, this.filter);
            if (nextNode == null) {
                continue;
            }
            List<TreeNode<T, R>> nextNodes = nextNode.nextNodes;
            if (nextNodes != null && !nextNodes.isEmpty()) {
                nextNodes.forEach(node -> trModels.add(node.getNodeModel(this.children, this.filter)));
            }
        }
        return trModels;
    }

    public static class TreeNode<T, R> {
        /**
         * 当前节点主标识
         */
        private final R code;
        /**
         * 上级数据主标识
         */
        private final R superCode;
        /**
         * 当前节点真实数据
         */
        private final T nodeData;
        /**
         * 上级数据引用
         */
        private TreeNode<T, R> superNode;
        /**
         * 下级节点集合
         */
        private List<TreeNode<T, R>> nextNodes;

        public TreeNode(T data, R value, R superValue, Supplier<T> newInstance, Consumer<T> operate) {
            this.code = value;
            this.superCode = superValue;
            if (newInstance != null) {
                this.nodeData = newInstance.get();

            } else {
                this.nodeData = data;
            }
            if (operate != null) {
                operate.accept(this.nodeData);
            }
        }

        /**
         * 获取节点数据
         * @param children 下级数据填充
         * @return 节点数据
         */
        private T getNodeModel(BiConsumer<T, List<T>> children, Predicate<TreeNode<T, R>> filter) {
            if (this.nodeData == null) {
                return null;
            }
            if (filter != null && !filter.test(this)) {
                return null;
            }
            if (this.nextNodes != null && !this.nextNodes.isEmpty()) {
                List<T> models = new ArrayList<>();
                for (TreeNode<T, R> nextNode : nextNodes) {
                    T nodeModel = nextNode.getNodeModel(children, filter);
                    if (nodeModel != null) {
                        models.add(nodeModel);
                    }
                }
                children.accept(this.nodeData, models);
            }
            return this.nodeData;
        }

        /**
         * 获取下级节点
         * @param value 节点主标识
         * @return 下级节点
         */
        private TreeNode<T, R> getNextNode(R value, Predicate<TreeNode<T, R>> filter) {
            if (Objects.deepEquals(this.code, value)) {
                if (filter != null && !filter.test(this)) {
                    return null;
                }
                return this;
            }
            if (this.nextNodes != null && !this.nextNodes.isEmpty()) {
                for (TreeNode<T, R> nextNode : this.nextNodes) {
                    TreeNode<T, R> model = nextNode.getNextNode(value, filter);
                    if (model != null) {
                        return model;
                    }
                }
            }
            return null;
        }

        /**
         * 判断是否包含此节点
         * @param value 节点主标识
         * @return true：包含
         */
        private boolean hasNode(R value, Predicate<TreeNode<T, R>> filter) {
            if (Objects.deepEquals(this.code, value)) {
                return filter == null || filter.test(this);
            }
            if (this.nextNodes != null && !this.nextNodes.isEmpty()) {
                for (TreeNode<T, R> nextNode : this.nextNodes) {
                    boolean hasNode = nextNode.hasNode(value, filter);
                    if (hasNode) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * 获取当前节点级别
         * @return 节点级别
         */
        private int getLevel(Predicate<TreeNode<T, R>> filter) {
            if (filter != null && !filter.test(this)) {
                return -1;
            }
            if (this.superNode == null) {
                return 1;
            }
            return this.superNode.getLevel(filter) + 1;
        }

        /**
         * 获取当前节点级别
         * @return 节点级别
         */
        public int getNoFilterLevel() {
            if (this.superNode == null) {
                return 1;
            }
            return this.superNode.getNoFilterLevel() + 1;
        }

        public TreeNode<T, R> getSuperNode() {
            return superNode;
        }

        public R getCode() {
            return code;
        }

        public R getSuperCode() {
            return superCode;
        }

        public T getNodeData() {
            return nodeData;
        }

        public List<TreeNode<T, R>> getNextNodes() {
            return nextNodes;
        }

        public void setNextNodes(List<TreeNode<T, R>> nextNodes) {
            if (nextNodes == null || nextNodes.isEmpty()) {
                return;
            }
            nextNodes.forEach(node -> node.superNode = this);
            this.nextNodes = nextNodes;
        }

        public boolean equals(TreeNode<T, R> node) {
            if (node == null) {
                return false;
            }
            return this.nodeData != null && this.nodeData.equals(node);
        }
    }
}

