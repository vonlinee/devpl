package org.apache.ddlutils.platform.mysql;

import lombok.Data;

import java.sql.Timestamp;

/**
 * CREATE TEMPORARY TABLE `TABLES` (
 * `TABLE_CATALOG` varchar(512) NOT NULL DEFAULT '',
 * `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
 * `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
 * `TABLE_TYPE` varchar(64) NOT NULL DEFAULT '',
 * `ENGINE` varchar(64) DEFAULT NULL,
 * `VERSION` bigint(21) unsigned DEFAULT NULL,
 * `ROW_FORMAT` varchar(10) DEFAULT NULL,
 * `TABLE_ROWS` bigint(21) unsigned DEFAULT NULL,
 * `AVG_ROW_LENGTH` bigint(21) unsigned DEFAULT NULL,
 * `DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
 * `MAX_DATA_LENGTH` bigint(21) unsigned DEFAULT NULL,
 * `INDEX_LENGTH` bigint(21) unsigned DEFAULT NULL,
 * `DATA_FREE` bigint(21) unsigned DEFAULT NULL,
 * `AUTO_INCREMENT` bigint(21) unsigned DEFAULT NULL,
 * `CREATE_TIME` datetime DEFAULT NULL,
 * `UPDATE_TIME` datetime DEFAULT NULL,
 * `CHECK_TIME` datetime DEFAULT NULL,
 * `TABLE_COLLATION` varchar(32) DEFAULT NULL,
 * `CHECKSUM` bigint(21) unsigned DEFAULT NULL,
 * `CREATE_OPTIONS` varchar(255) DEFAULT NULL,
 * `TABLE_COMMENT` varchar(2048) NOT NULL DEFAULT ''
 * ) ENGINE=MEMORY DEFAULT CHARSET=utf8
 */
@Data
public class InfoSchemaTable {

    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String tableType;
    private String engine;
    private Long version;
    private String rowFormat;
    private Long tableRows;
    private Long avgRowLength;
    private Long dataLength;
    private Long maxDataLength;
    private Long indexLength;
    private Long dataFree;
    private Long autoIncrement;
    private Timestamp createTime;
    private Timestamp updateTime;
    private Timestamp checkTime;
    private String tableCollation;
    private Long checksum;
    private String createOptions;
    private String tableComment;
}
