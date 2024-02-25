package io.devpl.backend.dao;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.devpl.backend.common.mvc.MyBatisPlusMapper;
import io.devpl.backend.entity.MappedStatementItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * MyBatis Mapper语句记录表
 **/
@Mapper
public interface MappedStatementItemMapper extends MyBatisPlusMapper<MappedStatementItem> {

    @Select("SELECT DISTINCT belong_file FROM mapped_statement_item")
    List<String> listBelongedFiles();

    @Select("SELECT DISTINCT project_root FROM mapped_statement_item")
    List<String> listIndexedProjectRootPaths();

    default boolean deleteByFile(Collection<String> filePaths) {
        LambdaUpdateWrapper<MappedStatementItem> dw = new LambdaUpdateWrapper<>();
        dw.in(MappedStatementItem::getBelongFile, filePaths);
        return retBool(delete(dw));
    }
}
