package io.devpl.tookit.fxui.vm;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.devpl.fxui.mapper.FieldSpecMapper;
import io.devpl.tookit.fxui.model.FieldSpec;
import io.devpl.tookit.fxui.view.TableData;

import java.util.List;

/**
 * 字段管理 ViewModel
 */
public class FieldSpecViewModel extends DBMapper {

    final FieldSpecMapper mapper = getMapper(FieldSpecMapper.class);

    public void saveBatch(List<FieldSpec> fieldSpecList) {
        mapper.insertBatch(fieldSpecList);
    }

    /**
     * 加载分页数据
     * @param pageIndex 第几页
     * @param pageSize  每页数据条数
     * @return 分页数据
     */
    public TableData<FieldSpec> listPage(int pageIndex, int pageSize) {
        IPage<FieldSpec> page = mapper.selectPage(new Page<>(pageIndex, pageSize), new LambdaQueryWrapper<>());
        return new TableData<>(page.getRecords(), true, page.getTotal());
    }
}
