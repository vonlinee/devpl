package io.devpl.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.devpl.backend.entity.DataTypeGroup;

import java.util.List;

public interface DataTypeGroupService extends IService<DataTypeGroup> {

    List<String> listAllGroupId();
}
