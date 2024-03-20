package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.TableFileGenerationMapper;
import io.devpl.backend.domain.param.TableFileGenParam;
import io.devpl.backend.entity.TableFileGeneration;
import io.devpl.backend.entity.TemplateFileGeneration;
import io.devpl.backend.service.TableFileGenerationService;
import io.devpl.backend.service.TemplateFileGenerationService;
import io.devpl.backend.service.TemplateService;
import io.devpl.sdk.util.CollectionUtils;
import io.devpl.sdk.util.StringUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 表文件生成关联信息
 */
@Service
public class TableFileGenerationServiceImpl extends ServiceImpl<TableFileGenerationMapper, TableFileGeneration> implements TableFileGenerationService {

    @Resource
    TemplateFileGenerationService templateFileGenerationService;
    @Resource
    TemplateService templateService;

    /**
     * 按表 ID 查询
     *
     * @param tableId 表 ID数组
     * @return {@link List}<{@link TableFileGeneration}>
     */
    @Override
    public List<TableFileGeneration> listByTableId(Long tableId) {
        return baseMapper.selectListByTableId(tableId);
    }

    /**
     * 按表 ID 查询
     *
     * @param tableIds 表 ID数组
     * @return {@link List}<{@link TableFileGeneration}>
     */
    @Override
    public List<TableFileGeneration> listByTableIds(Long[] tableIds) {
        LambdaQueryWrapper<TableFileGeneration> qw = new LambdaQueryWrapper<>();
        qw.in(TableFileGeneration::getTableId, Arrays.asList(tableIds));
        return baseMapper.selectList(qw);
    }

    /**
     * 按表 ID 删除
     *
     * @param tableIds 表 ID数组
     * @return boolean
     */
    @Override
    public boolean removeByTableIds(Long[] tableIds) {
        LambdaQueryWrapper<TableFileGeneration> qw = new LambdaQueryWrapper<>();
        qw.in(TableFileGeneration::getTableId, Arrays.asList(tableIds));
        return remove(qw);
    }

    /**
     * 按表 ID 逻辑删除
     *
     * @param tableIds 表 ID数组
     * @return boolean
     */
    @Override
    public boolean logicRemoveByTableIds(Long[] tableIds) {
        LambdaUpdateWrapper<TableFileGeneration> uw = new LambdaUpdateWrapper<>();
        uw.in(TableFileGeneration::getTableId, Arrays.asList(tableIds));
        uw.set(TableFileGeneration::isDeleted, true);
        return update(uw);
    }

    /**
     * 覆盖
     *
     * @param param 生成表ID
     * @return 是否成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateGeneratedFiles(TableFileGenParam param) {
        if (CollectionUtils.isEmpty(param.getFileInfoList())) {
            return true;
        }
        List<TableFileGeneration> fileInfoList = param.getFileInfoList();
        Set<Long> templateFileGenIds = CollectionUtils.toSet(fileInfoList, TableFileGeneration::getGenerationId);
        Map<Long, TemplateFileGeneration> map = CollectionUtils.toMap(templateFileGenerationService.listByIds(templateFileGenIds), TemplateFileGeneration::getId);
        Map<Long, String> templateIdNameMap = templateService.listIdAndNameMapByIds(null);
        for (TableFileGeneration tfg : fileInfoList) {
            TemplateFileGeneration templateFileGeneration = map.get(tfg.getGenerationId());
            if (templateFileGeneration == null) {
                continue;
            }
            if (!Objects.equals(tfg.getTemplateId(), templateFileGeneration.getTemplateId())) {
                templateFileGeneration.setTemplateId(tfg.getTemplateId());
                templateFileGeneration.setTemplateName(templateIdNameMap.get(tfg.getTemplateId()));
            }
            // 可能由于初始化时模板被删掉导致模板名称信息未填充
            if (!StringUtils.hasText(templateFileGeneration.getTemplateName())) {
                templateFileGeneration.setTemplateId(tfg.getTemplateId());
                templateFileGeneration.setTemplateName(templateIdNameMap.get(tfg.getTemplateId()));
            }
        }
        templateFileGenerationService.updateBatchById(map.values());
        return updateBatchById(fileInfoList);
    }

    /**
     * 按表 ID 删除，同时删除关联的文件生成信息
     *
     * @param tableIds    表 ID
     * @param logicDelete 逻辑删除
     * @return boolean
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeByTableIds(Long[] tableIds, boolean logicDelete) {
        // 删除关联的文件生成信息
        Set<Long> ids = CollectionUtils.toSet(listByTableIds(tableIds), TableFileGeneration::getGenerationId);
        boolean res = false;
        if (!CollectionUtils.isEmpty(ids)) {
            res = templateFileGenerationService.removeBatchByIds(ids);
        }
        if (logicDelete) {
            return logicRemoveByTableIds(tableIds);
        }
        return res & removeByTableIds(tableIds);
    }
}
