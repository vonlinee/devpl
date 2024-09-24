package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.devpl.backend.common.query.ListResult;
import io.devpl.backend.dao.DriverFileInfoMapper;
import io.devpl.backend.domain.param.DriverFileListParam;
import io.devpl.backend.entity.DriverFileInfo;
import io.devpl.backend.service.DriverService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverFileInfoMapper driverFileInfoMapper;

    @Override
    public ListResult<DriverFileInfo> listDrivers(DriverFileListParam param) {
        IPage<DriverFileInfo> page = driverFileInfoMapper.selectPage(param, Wrappers.emptyWrapper());
        return ListResult.ok(page);
    }
}
