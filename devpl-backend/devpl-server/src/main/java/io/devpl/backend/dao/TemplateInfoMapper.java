package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.domain.vo.TemplateSelectVO;
import io.devpl.backend.entity.TemplateInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface TemplateInfoMapper extends MyBatisPlusMapper<TemplateInfo> {

    /**
     * 查询模板ID和名称
     *
     * @return {@link List}<{@link TemplateSelectVO}>
     */
    List<TemplateSelectVO> selectTemplateIdAndNames();

    /**
     * 按模板 ID 批量删除
     *
     * @param templateIds 模板 ID 数组
     * @return int，删除条数
     */
    int deleteByIds(@Param("ids") Collection<Long> templateIds);
}
