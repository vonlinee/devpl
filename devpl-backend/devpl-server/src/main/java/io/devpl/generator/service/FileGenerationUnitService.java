package io.devpl.generator.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.generator.domain.param.FileGenUnitParam;
import io.devpl.generator.domain.vo.FileGenUnitVO;
import io.devpl.generator.entity.FileGenerationUnit;

import java.util.List;

public interface FileGenerationUnitService extends IService<FileGenerationUnit> {

    boolean addCustomFileGenUnit(FileGenerationUnit unit);

    boolean addTemplateFileGenUnits(FileGenUnitParam param);

    boolean addFileGenUnits(FileGenUnitParam param);

    List<FileGenUnitVO> listFileGenUnits();

    boolean removeFileGenUnit(FileGenUnitParam param);
}
