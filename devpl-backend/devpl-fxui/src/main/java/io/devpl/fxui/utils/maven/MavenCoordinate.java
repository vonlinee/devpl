package io.devpl.fxui.utils.maven;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MavenCoordinate {

    private String groupId;

    private String artifactId;

    private String version;

    private String packaging;
}
