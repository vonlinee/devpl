package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TableFileGenerationMapper;
import io.devpl.backend.domain.param.TableFileGenParam;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.service.TableFileGenerationService;
import io.devpl.sdk.util.CollectionUtils;
import org.springframework.stereotype.Service;

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
}
