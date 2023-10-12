package io.devpl.generator.domain.param;

import io.devpl.generator.entity.DataTypeItem;
import lombok.Data;

import java.util.Collection;

@Data
public class DataTypeAddParam {

    private Collection<DataTypeItem> dataTypeItems;
}
