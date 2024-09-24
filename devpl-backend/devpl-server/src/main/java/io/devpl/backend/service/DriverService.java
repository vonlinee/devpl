package io.devpl.backend.service;

import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.domain.param.DriverFileListParam;
import io.devpl.backend.entity.DriverFileInfo;

public interface DriverService {

    ListResult<DriverFileInfo> listDrivers(DriverFileListParam param);
}
