package io.devpl.backend.domain.param;

import io.devpl.backend.common.query.PageParam;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DbConnInfoListParam extends PageParam {

    private String connName;

    private String driverType;
}
