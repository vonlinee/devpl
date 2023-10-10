package io.devpl.generator.dao;

import io.devpl.generator.common.dao.BaseDao;
import io.devpl.generator.domain.vo.TemplateSelectVO;
import io.devpl.generator.entity.TemplateInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TemplateInfoDao extends BaseDao<TemplateInfo> {

    List<TemplateSelectVO> selectTemplateIdAndNames();
}
