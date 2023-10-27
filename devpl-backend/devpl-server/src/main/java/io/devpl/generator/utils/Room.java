package io.devpl.generator.utils;

import lombok.Data;

/**
 * 部门类（省略get set方法）
 **/
@Data
public class Room {
    /**
     * 区域ID
     */
    private String regionId;

    /**
     * 楼宇ID
     */
    private String buildingId;

    /**
     * 楼层
     */
    private String floor;

    /**
     * 房间号
     */
    private String roomName;
}
