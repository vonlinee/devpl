package org.apache.ddlutils.platform.mysql;

import lombok.Data;

/**
 * CREATE TEMPORARY TABLE `COLUMNS` (
 * `TABLE_CATALOG` varchar(512) NOT NULL DEFAULT '',
 * `TABLE_SCHEMA` varchar(64) NOT NULL DEFAULT '',
 * `TABLE_NAME` varchar(64) NOT NULL DEFAULT '',
 * `COLUMN_NAME` varchar(64) NOT NULL DEFAULT '',
 * `ORDINAL_POSITION` bigint(21) unsigned NOT NULL DEFAULT '0',
 * `COLUMN_DEFAULT` longtext,
 * `IS_NULLABLE` varchar(3) NOT NULL DEFAULT '',
 * `DATA_TYPE` varchar(64) NOT NULL DEFAULT '',
 * `CHARACTER_MAXIMUM_LENGTH` bigint(21) unsigned DEFAULT NULL,
 * `CHARACTER_OCTET_LENGTH` bigint(21) unsigned DEFAULT NULL,
 * `NUMERIC_PRECISION` bigint(21) unsigned DEFAULT NULL,
 * `NUMERIC_SCALE` bigint(21) unsigned DEFAULT NULL,
 * `DATETIME_PRECISION` bigint(21) unsigned DEFAULT NULL,
 * `CHARACTER_SET_NAME` varchar(32) DEFAULT NULL,
 * `COLLATION_NAME` varchar(32) DEFAULT NULL,
 * `COLUMN_TYPE` longtext NOT NULL,
 * `COLUMN_KEY` varchar(3) NOT NULL DEFAULT '',
 * `EXTRA` varchar(30) NOT NULL DEFAULT '',
 * `PRIVILEGES` varchar(80) NOT NULL DEFAULT '',
 * `COLUMN_COMMENT` varchar(1024) NOT NULL DEFAULT '',
 * `GENERATION_EXPRESSION` longtext NOT NULL
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8
 */
@Data
public class InfoSchemaColumn {

    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String columnName;
    private Long ordinalPosition;
    private String columnDefault;
    private String isNullable;
    private String dataType;
    private Long characterMaximumLength;
    private Long characterOctetLength;
    private Long numericPrecision;
    private Long numericScale;
    private Long datetimePrecision;
    private String characterSetName;
    private String collationName;
    private String columnType;
    private String columnKey;
    private String extra;
    private String privileges;
    private String columnComment;
    private String generationExpression;
}
