package io.devpl.codegen.generator;

import io.devpl.codegen.generator.config.PropertyObject;

/**
 * This class holds constants for all properties recognized by the different
 * configuration elements. This helps document and maintain the different
 * properties, and helps to avoid spelling errors.
 *
 * @see PropertyObject
 */
public interface PropertyRegistry {
    String ANY_ENABLE_SUB_PACKAGES = "enableSubPackages";

    /**
     * recognized by table and java model generator.
     */
    String ANY_ROOT_CLASS = "rootClass";
    String ANY_IMMUTABLE = "immutable";
    String ANY_CONSTRUCTOR_BASED = "constructorBased";
    /**
     * recognized by table and java client generator.
     */
    String ANY_ROOT_INTERFACE = "rootInterface";

    String TABLE_USE_COLUMN_INDEXES = "useColumnIndexes";
    String TABLE_USE_ACTUAL_COLUMN_NAMES = "useActualColumnNames";
    String TABLE_USE_COMPOUND_PROPERTY_NAMES = "useCompoundPropertyNames";
    String TABLE_IGNORE_QUALIFIERS_AT_RUNTIME = "ignoreQualifiersAtRuntime";
    String TABLE_RUNTIME_CATALOG = "runtimeCatalog";
    String TABLE_RUNTIME_SCHEMA = "runtimeSchema";
    String TABLE_RUNTIME_TABLE_NAME = "runtimeTableName";
    String TABLE_MODEL_ONLY = "modelOnly";
    String TABLE_SELECT_ALL_ORDER_BY_CLAUSE = "selectAllOrderByClause";
    String TABLE_DYNAMIC_SQL_SUPPORT_CLASS_NAME = "dynamicSqlSupportClassName";
    String TABLE_DYNAMIC_SQL_TABLE_OBJECT_NAME = "dynamicSqlTableObjectName";

    String CONTEXT_BEGINNING_DELIMITER = "beginningDelimiter";
    String CONTEXT_ENDING_DELIMITER = "endingDelimiter";
    String CONTEXT_AUTO_DELIMIT_KEYWORDS = "autoDelimitKeywords";
    String CONTEXT_JAVA_FILE_ENCODING = "javaFileEncoding";
    String CONTEXT_JAVA_FORMATTER = "javaFormatter";
    String CONTEXT_XML_FORMATTER = "xmlFormatter";
    String CONTEXT_KOTLIN_FORMATTER = "kotlinFormatter";
    String CONTEXT_KOTLIN_FILE_ENCODING = "kotlinFileEncoding";

    String CLIENT_DYNAMIC_SQL_SUPPORT_PACKAGE = "dynamicSqlSupportPackage";

    String TYPE_RESOLVER_FORCE_BIG_DECIMALS = "forceBigDecimals";
    String TYPE_RESOLVER_USE_JSR310_TYPES = "useJSR310Types";

    String MODEL_GENERATOR_TRIM_STRINGS = "trimStrings";
    String MODEL_GENERATOR_EXAMPLE_PACKAGE = "exampleTargetPackage";
    String MODEL_GENERATOR_EXAMPLE_PROJECT = "exampleTargetProject";

    String COMMENT_GENERATOR_SUPPRESS_DATE = "suppressDate";
    String COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS = "suppressAllComments";
    String COMMENT_GENERATOR_ADD_REMARK_COMMENTS = "addRemarkComments";
    String COMMENT_GENERATOR_DATE_FORMAT = "dateFormat";
    String COMMENT_GENERATOR_USE_LEGACY_GENERATED_ANNOTATION = "useLegacyGeneratedAnnotation";

    String COLUMN_OVERRIDE_FORCE_JAVA_TYPE = "forceJavaTypeIntoMapping";

    String INTROSPECTED_COLUMN_IMPL = "introspectedColumnImpl";
    String TARGET_RUNTIME = "targetRuntime";
    String MODEL_TYPE = "modelType";
    String CONTEXT_IMPL = "contextImpl";
}
