package io.devpl.backend.domain.param;

import io.devpl.backend.entity.DataTypeItem;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class DataTypeAddParam {

    private Collection<DataTypeItem> dataTypeItems;
}
