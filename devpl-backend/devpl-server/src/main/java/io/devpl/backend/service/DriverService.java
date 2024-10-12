package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.DriverFileListParam;
import io.devpl.backend.entity.DriverFileInfo;

public interface DriverService extends IService<DriverFileInfo> {

    ListResult<DriverFileInfo> listDrivers(DriverFileListParam param);
}
