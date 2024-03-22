package io.devpl.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.devpl.backend.dao.DataTypeMappingMapper;
import io.devpl.backend.entity.DataTypeMapping;
import io.devpl.backend.service.DataTypeMappingService;
import org.springframework.stereotype.Service;

@Service
public class DataTypeMappingServiceImpl extends ServiceImpl<DataTypeMappingMapper, DataTypeMapping> implements DataTypeMappingService {
}
