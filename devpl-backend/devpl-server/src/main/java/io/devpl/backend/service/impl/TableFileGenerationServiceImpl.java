package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TableFileGenerationMapper;
import io.devpl.backend.domain.param.TableFileGenParam;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.service.TableFileGenerationService;
import io.devpl.sdk.util.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class TableFileGenerationServiceImpl extends ServiceImpl<TableFileGenerationMapper, TableFileGeneration> implements TableFileGenerationService {

    @Override
    public List<TableFileGeneration> listByTableId(Long tableId) {
        return baseMapper.selectListByTableId(tableId);
    }

    /**
     * 覆盖
     *
     * @param param 生成表ID
     * @return 是否成功
     */
    @Override
    public boolean updateFilesToBeGenerated(TableFileGenParam param) {
        if (CollectionUtils.isEmpty(param.getFileInfoList())) {
            return true;
        }
        return updateBatchById(param.getFileInfoList());
    }

    @Override
    public boolean removeByTableIds(Long[] tableIds, boolean logicDelete) {
        if (logicDelete) {
            LambdaUpdateWrapper<TableFileGeneration> uw = new LambdaUpdateWrapper<>();
            uw.in(TableFileGeneration::getTableId, Arrays.asList(tableIds));
            uw.set(TableFileGeneration::isDeleted, true);
            return update(uw);
        }
        LambdaQueryWrapper<TableFileGeneration> qw = new LambdaQueryWrapper<>();
        qw.in(TableFileGeneration::getTableId, Arrays.asList(tableIds));
        return remove(qw);
    }
}
