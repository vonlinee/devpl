package io.devpl.generator.utils;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Data
public class TreeBuilder<F, T> {
    /**
     * 平铺的源数据列表
     */
    List<F> rawList;
    /**
     * 每一层的构造器，从0开始为根节点
     */
    List<Function<F, T>> levelConverters = new ArrayList<>();
    /**
     * 每一层的唯一标识
     */
    List<Function<F, ?>> levelKeyFunction = new ArrayList<>();
    /**
     * 第一个为父节点，第二个为即将添加的子节点
     */
    BiConsumer<T, T> consumer;

    Object[] prevKeys;
    int[] preveIndices;

    public void addLevelConverter(Function<F, T> mapper) {
        levelConverters.add(mapper);
    }

    public void addLevelKeyMapper(Function<F, ?> mapper) {
        levelKeyFunction.add(mapper);
    }

    List<T> results = new ArrayList<>();

    public void build() {
        // 构建第一条路径
        F firstItem = rawList.get(0);
        T prev = null;
        for (int i = 0; i < levelConverters.size(); i++) {
            T current = levelConverters.get(i).apply(firstItem);
            if (prev == null) {
                results.add(current);
            } else {
                consumer.accept(prev, current);
            }
            prev = current;
        }

        // 层数 - 1
        prevKeys = new Object[levelConverters.size()];
        for (int i = 0; i < prevKeys.length - 1; i++) {
            prevKeys[i] = levelKeyFunction.get(i).apply(firstItem);
        }

        preveIndices = new int[levelConverters.size()];

        // 遍历剩下的元素
        for (int i = 1; i < rawList.size(); i++) {
            F current = rawList.get(i);
            recursive(current, 0);
        }
    }

    public void recursive(F current, int level) {
        if (level > levelConverters.size() - 1) {
            // 到了叶子结点
            return;
        }
        if (isKeyEquals(getLevelKey(current, level), level)) {
            recursive(current, level + 1);
        } else {
            // 创建下一级
            // 更新索引信息
            preveIndices[level]++;
            for (int i = level + 1; i < levelConverters.size(); i++) {
                preveIndices[i] = 0;
            }
            // 更新key值
            prevKeys[level] = levelKeyFunction.get(level).apply(current);
            // 添加剩下的路径
            for (int i = level; i < levelConverters.size(); i++) {
                T item = levelConverters.get(i).apply(current);
                if (i == 0) {
                    results.add(item);
                } else {
                    
                }
            }
        }
    }

    /**
     * @param currentLevelKey 可能为null
     * @param level           当前层级
     * @return 是否属于当前层级
     */
    public boolean isKeyEquals(Object currentLevelKey, int level) {
        return Objects.equals(prevKeys[level], currentLevelKey);
    }

    public Object getLevelKey(F current, int level) {
        if (level > levelKeyFunction.size() - 1) {
            return null;
        }
        return levelKeyFunction.get(level).apply(current);
    }

    /**
     * 每新建一个分支都要构建一条从根节点到叶子结点的路径
     */
    public void treeify(List<Room> rawList) {
        List<CascadeVO> results = new ArrayList<>();

        Room firstItem = rawList.get(0);

        // 先构建第一条路径
        CascadeVO firstRegion = new CascadeVO();
        firstRegion.setItemId(firstItem.getRegionId());
        firstRegion.setLabel(firstItem.getRegionId());
        results.add(firstRegion);

        CascadeVO firstBuilding = new CascadeVO();
        firstBuilding.setLabel(firstItem.getBuildingId());
        firstBuilding.setItemId(firstItem.getBuildingId());
        firstRegion.addChild(firstBuilding);

        CascadeVO firstFloor = new CascadeVO();
        firstFloor.setLabel(firstItem.getFloor());
        firstFloor.setItemId(firstItem.getFloor());
        firstBuilding.addChild(firstFloor);

        CascadeVO firstRoom = new CascadeVO();
        firstRoom.setLabel(firstItem.getRoomName());
        firstRoom.setItemId(firstItem.getRoomName());
        firstFloor.addChild(firstRoom);

        // 每层的都有一个父节点
        String previousRegion = firstItem.getRegionId();
        String previouseBuiding = firstItem.getBuildingId();
        String previouesFloor = firstItem.getFloor();

        int preRegionIndex = 0;
        int preBuildingIndex = 0;
        int preFloorIndex = 0;
        // 从第一个元素开始
        final int size = rawList.size();
        for (int i = 1; i < size; i++) {
            Room item = rawList.get(i);
            if (Objects.equals(item.getRegionId(), previousRegion)) {
                // 第一级是同一级
                // 开始下一级
                if (Objects.equals(item.getBuildingId(), previouseBuiding)) {
                    if (Objects.equals(item.getFloor(), previouesFloor)) {
                        CascadeVO vo = new CascadeVO();
                        vo.setItemId(item.getRoomName());
                        vo.setLabel(item.getRoomName());
                        results.get(preRegionIndex).getChildren().get(preBuildingIndex).getChildren().get(preFloorIndex).addChild(vo);
                    } else {
                        previouesFloor = item.getFloor();
                        preFloorIndex += 1;

                        // 添加楼层
                        CascadeVO cascadeVO = new CascadeVO();
                        cascadeVO.setItemId(item.getFloor());
                        cascadeVO.setLabel(item.getFloor());
                        results.get(preRegionIndex).getChildren().get(preBuildingIndex).addChild(cascadeVO);

                        // 添加房间
                        CascadeVO currentRoom = new CascadeVO();
                        currentRoom.setItemId(item.getRoomName());
                        currentRoom.setLabel(item.getRoomName());
                        results.get(preRegionIndex).getChildren().get(preBuildingIndex).getChildren().get(preFloorIndex).addChild(currentRoom);
                    }
                } else {
                    // 添加楼宇
                    CascadeVO vo = new CascadeVO();
                    vo.setLabel(item.getBuildingId());
                    vo.setItemId(item.getBuildingId());
                    preBuildingIndex += 1;
                    preFloorIndex = 0;
                    previouseBuiding = item.getBuildingId();
                    results.get(preRegionIndex).addChild(vo);

                    // 添加楼层
                    CascadeVO cascadeVO = new CascadeVO();
                    cascadeVO.setItemId(item.getFloor());
                    cascadeVO.setLabel(item.getFloor());
                    results.get(preRegionIndex).getChildren().get(preBuildingIndex).addChild(cascadeVO);

                    // 添加房间
                    CascadeVO cascadeVO3 = new CascadeVO();
                    cascadeVO3.setItemId(item.getRoomName());
                    cascadeVO3.setLabel(item.getRoomName());
                    results.get(preRegionIndex).getChildren().get(preBuildingIndex).getChildren().get(0).addChild(cascadeVO3);
                }
            } else {
                preRegionIndex += 1;
                preBuildingIndex = 0;
                preFloorIndex = 0;
                previousRegion = item.getRegionId();
                // 添加区域
                CascadeVO vo = new CascadeVO();
                vo.setLabel(item.getRegionId());
                vo.setItemId(item.getRegionId());
                results.add(vo);
                // 添加楼宇
                CascadeVO vo1 = new CascadeVO();
                vo1.setLabel(item.getBuildingId());
                vo1.setItemId(item.getBuildingId());
                results.get(preRegionIndex).addChild(vo1);
                // 添加楼层
                CascadeVO vo2 = new CascadeVO();
                vo2.setItemId(item.getFloor());
                vo2.setLabel(item.getFloor());
                results.get(preRegionIndex).getChildren().get(0).addChild(vo2);
                // 添加房间
                CascadeVO vo3 = new CascadeVO();
                vo3.setItemId(item.getRoomName());
                vo3.setLabel(item.getRoomName());
                results.get(preRegionIndex).getChildren().get(0).getChildren().get(0).addChild(vo3);
            }
        }

        JSONUtils.writeFile(results, new File("D:/Temp/1.json"));
    }
}
