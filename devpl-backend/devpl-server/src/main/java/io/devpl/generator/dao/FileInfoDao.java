package io.devpl.generator.dao;

import io.devpl.generator.common.mvc.BaseDao;
import io.devpl.generator.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileInfoDao extends BaseDao<FileInfo> {

}
