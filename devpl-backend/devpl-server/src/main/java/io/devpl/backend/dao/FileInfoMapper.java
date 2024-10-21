package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileInfoMapper extends BaseMapper<FileInfo>, MyBatisPlusMapper<FileInfo> {

}
