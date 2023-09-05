package io.devpl.generator.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 将List中存在父子节点关系的转化为树结构数据 先创建两个set方法的抽象类
 * <p>
 * TODO 每层的ID可以动态变化
 * @param <T> 源list
 * @param <K> 具有树形结构的对象
 * @apiNote 另一种方案：遍历列表，使用Map来存储每层的索引信息
 */
public class TreeBuilder<T, K> {

    private final List<T> list;

    /**
     * 获取每层唯一ID
     */
    private final Function<? super T, ? extends K> getId;

    /**
     * 获取父层ID
     */
    private final Function<? super T, ? extends K> getParentId;

    /**
     * 用来注入子节点数据
     */
    private final BiConsumer<T, List<T>> setChildren;

    /**
     * 用来注入当前节点的楼数，当然这个可以用也可以不用
     */
    private BiConsumer<T, Integer> setFloor;

    /**
     * 这个是不用楼数的
     * @param list        需要转换的list数据
     * @param getId       节点的id
     * @param getParentId 节点的父id
     * @param setChildren 注入子节点的方法
     */
    public TreeBuilder(List<T> list, Function<? super T, ? extends K> getId, Function<? super T, ? extends K> getParentId, BiConsumer<T, List<T>> setChildren) {
        this.list = list;
        this.getId = getId;
        this.getParentId = getParentId;
        this.setChildren = setChildren;
    }

    /**
     * 这个是使用楼数的
     * @param list        需要转换的list数据
     * @param getId       节点的id
     * @param getParentId 节点的父id
     * @param setChildren 注入子节点的方法
     * @param setFloor    注入当前节点处于整棵树层数的方法
     */
    public TreeBuilder(List<T> list, Function<? super T, ? extends K> getId, Function<? super T, ? extends K> getParentId, BiConsumer<T, List<T>> setChildren, BiConsumer<T, Integer> setFloor) {
        this.list = list;
        this.getId = getId;
        this.getParentId = getParentId;
        this.setChildren = setChildren;
        this.setFloor = setFloor;
    }

    /**
     * 开始构建节点
     * @return list 所有根节点的集合
     */
    public List<T> build() {
        if (this.list.isEmpty()) {
            return null;// 这里也可以有其他的处理方式
        }
        List<T> rootList = new ArrayList<>();
        // 假设里面的每个元素都是根节点
        for (T root : this.list) {
            // 如果自己的id在所有元素的parentId中都不存在，则可以认定该元素是一个根节点
            if (this.list.stream().noneMatch(item -> getId.apply(item).equals(getParentId.apply(root)))) {
                rootList.add(buildChild(root, 1));
            }
        }
        return rootList;
    }

    /**
     * 使用递归开始构建子节点
     * @param parent 父节点
     * @param floor  层数
     * @return T 构建好当前节点的所有子节点
     */
    private T buildChild(T parent, int floor) {
        List<T> children = new ArrayList<>();
        if (setFloor != null) {
            setFloor.accept(parent, floor);
        }
        for (T t : list) {
            if (getId.apply(parent).equals(getParentId.apply(t))) {
                children.add(buildChild(t, (floor + 1)));
            }
        }
        // 找到所有子节点后将其注入到父节点
        setChildren.accept(parent, children);
        return parent;
    }
}
