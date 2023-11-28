package io.devpl.backend.domain.param;

import io.devpl.backend.entity.DataTypeItem;
import lombok.Data;

import java.util.Collection;

@Data
public class DataTypeAddParam {

    private Collection<DataTypeItem> dataTypeItems;
}
