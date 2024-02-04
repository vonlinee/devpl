package io.devpl.backend.dao;

import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.domain.vo.TemplateSelectVO;
import io.devpl.backend.entity.TemplateInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TemplateInfoMapper extends MyBatisPlusMapper<TemplateInfo> {

    List<TemplateSelectVO> selectTemplateIdAndNames();
}
