package io.devpl.generator.service.impl;

import io.devpl.generator.dao.TargetGenFileMapper;
import io.devpl.generator.entity.TargetGenFile;
import io.devpl.generator.service.CrudService;
import io.devpl.generator.service.TargetGenFileService;
import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板文件生成关联表
 */
@Service
@AllArgsConstructor
public class TargetGenFileServiceImpl implements TargetGenFileService {

    @Resource
    TargetGenFileMapper targetGenFileMapper;
    @Resource
    CrudService crudService;

    @Override
    public boolean saveOrUpdate(TargetGenFile entity) {
        if (entity.getId() == null) {
            return targetGenFileMapper.insert(entity) > 0;
        }
        return targetGenFileMapper.updateById(entity) > 0;
    }

    @Override
    public List<TargetGenFile> listGeneratedFileTypes() {
        return targetGenFileMapper.selectGeneratedFileTypes();
    }

    @Override
    public boolean removeBatchByIds(List<Integer> ids) {
        return targetGenFileMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean saveOrUpdateBatch(List<TargetGenFile> files) {
        return crudService.saveOrUpdateBatch(files);
    }
}
