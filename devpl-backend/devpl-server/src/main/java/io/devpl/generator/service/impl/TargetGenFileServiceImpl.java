package io.devpl.generator.service.impl;

import io.devpl.generator.common.mvc.BaseServiceImpl;
import io.devpl.generator.dao.TargetGenFileMapper;
import io.devpl.generator.entity.TargetGenFile;
import io.devpl.generator.service.TargetGenFileService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模板文件生成关联表
 */
@Service
@AllArgsConstructor
public class TargetGenFileServiceImpl extends BaseServiceImpl<TargetGenFileMapper, TargetGenFile> implements TargetGenFileService {

    @Override
    public List<TargetGenFile> listGeneratedFileTypes() {
        return baseMapper.selectGeneratedFileTypes();
    }
}
