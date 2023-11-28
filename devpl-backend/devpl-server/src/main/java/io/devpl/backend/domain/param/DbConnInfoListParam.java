package io.devpl.backend.domain.param;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DbConnInfoListParam extends Query {

    private String connName;

    private String driverType;
}
