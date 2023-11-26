package io.devpl.generator.service.impl;

import io.devpl.generator.dao.TargetGenerationFileMapper;
import io.devpl.generator.entity.TargetGenerationFile;
import io.devpl.generator.service.CrudService;
import io.devpl.generator.service.TargetGenerationFileService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板文件生成关联表
 */
@Service
@AllArgsConstructor
public class TargetGenerationFileServiceImpl implements TargetGenerationFileService {

    @Resource
    TargetGenerationFileMapper targetGenFileMapper;
    @Resource
    CrudService crudService;

    @Override
    public boolean saveOrUpdate(TargetGenerationFile entity) {
        if (entity.getId() == null) {
            return targetGenFileMapper.insert(entity) > 0;
        }
        return targetGenFileMapper.updateById(entity) > 0;
    }

    @Override
    public List<TargetGenerationFile> listGeneratedFileTypes() {
        return targetGenFileMapper.selectGeneratedFileTypes();
    }

    @Override
    public boolean removeBatchByIds(List<Integer> ids) {
        return targetGenFileMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean saveOrUpdateBatch(List<TargetGenerationFile> files) {
        return crudService.saveOrUpdateBatch(files);
    }
}
