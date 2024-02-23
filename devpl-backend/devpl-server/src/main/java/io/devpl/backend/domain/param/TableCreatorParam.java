package io.devpl.backend.domain.param;

import io.devpl.backend.entity.FieldInfo;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class TableCreatorParam {

    @Nullable
    private Long groupId;

    private List<FieldInfo> fields;
}
