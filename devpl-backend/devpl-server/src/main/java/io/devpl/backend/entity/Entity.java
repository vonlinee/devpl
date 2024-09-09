package io.devpl.backend.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 所有数据库实体类父类，放一些通用字段
 */
public abstract class Entity {

    /**
     * 更新时间
     */
    @Getter
    @JsonIgnore
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @Getter
    @JsonIgnore
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 是否逻辑删除
     */
    @JsonIgnore
    @TableField(value = "is_deleted")
    private Boolean deleted;

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted != null && deleted;
    }

    public boolean isDeleted() {
        return deleted != null && deleted;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime == null ? LocalDateTime.now() : updateTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime == null ? LocalDateTime.now() : createTime;
    }
}
