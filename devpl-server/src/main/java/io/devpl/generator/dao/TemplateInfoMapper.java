package io.devpl.generator.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.generator.domain.vo.TemplateSelectVO;
import io.devpl.generator.entity.TemplateInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TemplateInfoMapper extends BaseMapper<TemplateInfo> {

    List<TemplateSelectVO> selectTemplateIdAndNames();
}
