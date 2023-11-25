package io.devpl.generator.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.generator.dao.FileGenerationUnitMapper;
import io.devpl.generator.dao.TemplateFileGenerationMapper;
import io.devpl.generator.domain.param.FileGenUnitParam;
import io.devpl.generator.domain.vo.FileGenUnitVO;
import io.devpl.generator.entity.FileGenerationUnit;
import io.devpl.generator.entity.TemplateFileGeneration;
import io.devpl.generator.service.FileGenerationUnitService;
import io.devpl.sdk.util.CollectionUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class FileGenerationUnitServiceImpl extends ServiceImpl<FileGenerationUnitMapper, FileGenerationUnit> implements FileGenerationUnitService {

    @Resource
    TemplateFileGenerationMapper templateFileGenerationMapper;

    @Override
    public boolean addCustomFileGenUnit(FileGenerationUnit unit) {
        return save(unit);
    }

    @Override
    public boolean addTemplateFileGenUnits(FileGenUnitParam param) {
        for (TemplateFileGeneration tfg : param.getTfgs()) {
            tfg.setUnitId(param.getId());
            templateFileGenerationMapper.insert(tfg);
        }
        return true;
    }

    @Override
    public boolean addFileGenUnits(FileGenUnitParam param) {

        param.getTfgs();

        return true;
    }

    @Override
    public List<FileGenUnitVO> listFileGenUnits() {

        List<TemplateFileGeneration> tfgList = templateFileGenerationMapper.selectList(Wrappers.emptyWrapper());

        List<FileGenerationUnit> fgs = this.list();

        Map<Long, FileGenerationUnit> map = CollectionUtils.toMap(fgs, FileGenerationUnit::getId);

        List<FileGenUnitVO> result = new ArrayList<>();

        if (CollectionUtils.isEmpty(tfgList)) {
            for (FileGenerationUnit fgu : fgs) {
                FileGenUnitVO vo = new FileGenUnitVO();
                vo.setId(fgu.getId());
                vo.setParentId(null);
                vo.setItemName(fgu.getUnitName());
                result.add(vo);
            }
        } else {
            for (TemplateFileGeneration tfg : tfgList) {
                FileGenUnitVO vo = new FileGenUnitVO();
                if (tfg.getUnitId() != null && map.containsKey(tfg.getUnitId())) {
                    vo.setId(tfg.getId());
                    vo.setParentId(tfg.getUnitId());
                }
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public boolean removeFileGenUnit(FileGenUnitParam param) {
        if (param.getType() == 1) {
            // 移除文件生成单元及该单元下的所有文件生成记录
            return removeById(param.getId()) && templateFileGenerationMapper.deleteByUnitId(param.getId()) > 0;
        } else if (param.getType() == 2) {
            return templateFileGenerationMapper.deleteById(param.getId()) > 0;
        }
        return true;
    }
}
