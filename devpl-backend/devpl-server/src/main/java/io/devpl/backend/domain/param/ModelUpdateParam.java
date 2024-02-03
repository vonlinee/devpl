package io.devpl.backend.domain.param;

import lombok.Data;

import java.util.Collection;

@Data
public class ModelUpdateParam {

    private Long modelId;

    private Collection<Long> fieldIds;
}
