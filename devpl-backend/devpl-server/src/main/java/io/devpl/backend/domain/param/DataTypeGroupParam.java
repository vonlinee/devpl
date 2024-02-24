package io.devpl.backend.domain.param;

import io.devpl.backend.entity.DataTypeGroup;
import lombok.Data;

import java.util.List;

@Data
public class DataTypeGroupParam {

    private List<DataTypeGroup> groups;
}
