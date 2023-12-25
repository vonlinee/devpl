package io.devpl.fxui.model;

import lombok.Data;

/**
 * 数据库信息
 */
@Data
public class DatabaseInfo {

    private String name;
    private Integer id;
    private String dbType;
    private String host;
    private String port;
    private String schema;
    private String username;
    private String password;
    private String encoding;
    private String lport;
    private String rport;
    private String sshPort;
    private String sshHost;
    private String sshUser;
    private String sshPassword;
    private String privateKeyPassword;
    private String privateKey;
}
