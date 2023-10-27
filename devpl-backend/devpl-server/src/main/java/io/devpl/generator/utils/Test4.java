package io.devpl.generator.utils;

import io.devpl.generator.mock.RandomHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * 模拟七个部门，分三个层级
 */
public class Test4 {

    public static List<Room> roomVOList() {
        RandomHelper randomHelper = new RandomHelper();
        List<Room> list = new ArrayList<>();
        // 区域
        for (int i = 1; i < 4; i++) {
            // 楼宇
            int buildingCount = randomHelper.randomInt(4, 7);
            for (int j = 1; j <= buildingCount; j++) {
                // 楼层
                int floorCount = randomHelper.randomInt(4, 8);
                for (int k = 1; k <= floorCount; k++) {
                    // 房间
                    int roomCount = randomHelper.randomInt(9, 16);
                    for (int l = 1; l <= roomCount; l++) {
                        Room roomVO = new Room();
                        roomVO.setRegionId("区域" + i);
                        roomVO.setBuildingId(j + "栋");
                        roomVO.setFloor(k + "楼");
                        roomVO.setRoomName(j + "" + l + "室");
                        list.add(roomVO);
                    }
                }
            }
        }
        return list;
    }

    // 普通对象，没有树形结构

    public static void main(String[] args) {
        List<Room> list = roomVOList();

        TreeBuilder<Room, CascadeVO> builder = new TreeBuilder<>();
        // builder.treeify(list);

        builder.addLevelConverter(room -> {
            CascadeVO cascadeVO = new CascadeVO();
            cascadeVO.setItemId(room.getRegionId());
            cascadeVO.setLabel(room.getRegionId());
            return cascadeVO;
        });
        builder.addLevelConverter(room -> {
            CascadeVO cascadeVO = new CascadeVO();
            cascadeVO.setItemId(room.getBuildingId());
            cascadeVO.setLabel(room.getBuildingId());
            return cascadeVO;
        });
        builder.addLevelConverter(room -> {
            CascadeVO cascadeVO = new CascadeVO();
            cascadeVO.setItemId(room.getFloor());
            cascadeVO.setLabel(room.getFloor());
            return cascadeVO;
        });
        builder.addLevelConverter(room -> {
            CascadeVO cascadeVO = new CascadeVO();
            cascadeVO.setItemId(room.getRoomName());
            cascadeVO.setLabel(room.getRoomName());
            return cascadeVO;
        });

        builder.addLevelKeyMapper(Room::getRegionId);
        builder.addLevelKeyMapper(Room::getBuildingId);
        builder.addLevelKeyMapper(Room::getFloor);

        builder.setConsumer(CascadeVO::addChild);

        builder.setRawList(list);

        builder.build();
    }
}

