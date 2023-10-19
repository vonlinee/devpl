package io.devpl.fxui.utils.maven;

import lombok.Data;

@Data
public class MavenCoordinate {

    private String groupId;

    private String artifactId;

    private String version;

    private String packaging;
}
