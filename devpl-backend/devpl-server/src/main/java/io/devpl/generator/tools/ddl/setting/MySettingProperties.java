package io.devpl.generator.tools.ddl.setting;

import io.devpl.generator.tools.ddl.enums.SqlTypeAndJavaTypeEnum;

/**
 * 配置数据
 */
public class MySettingProperties {
    /**
     * 是否开启自动翻译
     */
    private Boolean autoTranslationRadio = false;
    /**
     * 翻译组件
     */
    private String translationAppComboBox = "";
    /**
     * appid
     */
    private String appIdText = "";
    /**
     * secret
     */
    private String secretText = "";
    /**
     * 腾讯云翻译secretId
     */
    private String secretId = "";
    /**
     * 腾讯云翻译secretKey
     */
    private String secretKey = "";
    /**
     * 表名使用的注解
     */
    private String tableAnnotation = "javax.persistence.Table";
    /**
     * 表名使用的注解属性
     */
    private String tableAnnotationProperty = "name";
    /**
     * id使用的注解
     */
    private String idAnnotation = "javax.persistence.Id";
    /**
     * 注释
     */
    private String commentAnnotation = "comment";

    private String intType = SqlTypeAndJavaTypeEnum.INT.getSqlType();

    private String longType = SqlTypeAndJavaTypeEnum.BIGINT.getSqlType();

    private String stringType = SqlTypeAndJavaTypeEnum.VARCHAR.getSqlType();

    private String booleanType = SqlTypeAndJavaTypeEnum.TINYINT.getSqlType();

    private String dateType = SqlTypeAndJavaTypeEnum.DATETIME.getSqlType();

    private String doubleType = SqlTypeAndJavaTypeEnum.DOUBLE.getSqlType();

    private String floatType = SqlTypeAndJavaTypeEnum.DOUBLE.getSqlType();

    private String bigDecimalType = SqlTypeAndJavaTypeEnum.DECIMAL.getSqlType();

    private String localDateType = SqlTypeAndJavaTypeEnum.DATE.getSqlType();

    private String localTimeType = SqlTypeAndJavaTypeEnum.TIME.getSqlType();

    private String localDateTimeType = SqlTypeAndJavaTypeEnum.TIMESTAMP.getSqlType();

    private String intDefaultLength = SqlTypeAndJavaTypeEnum.INT.getDefaultLength();

    private String longDefaultLength = SqlTypeAndJavaTypeEnum.BIGINT.getDefaultLength();

    private String stringDefaultLength = SqlTypeAndJavaTypeEnum.VARCHAR.getDefaultLength();

    private String doubleDefaultLength = SqlTypeAndJavaTypeEnum.DOUBLE.getDefaultLength();

    private String floatDefaultLength = SqlTypeAndJavaTypeEnum.DOUBLE.getDefaultLength();

    private String booleanDefaultLength = SqlTypeAndJavaTypeEnum.TINYINT.getDefaultLength();

    private String dateDefaultLength = SqlTypeAndJavaTypeEnum.DATETIME.getDefaultLength();

    private String bigDecimalDefaultLength = SqlTypeAndJavaTypeEnum.DECIMAL.getDefaultLength();

    private String localDateDefaultLength = SqlTypeAndJavaTypeEnum.DATE.getDefaultLength();

    private String localTimeDefaultLength = SqlTypeAndJavaTypeEnum.TIME.getDefaultLength();

    private String localDateTimeDefaultLength = SqlTypeAndJavaTypeEnum.TIMESTAMP.getDefaultLength();

    private Boolean showNoInMapFieldRadio = false;

    public Boolean getAutoTranslationRadio() {
        return autoTranslationRadio;
    }

    public void setAutoTranslationRadio(Boolean autoTranslationRadio) {
        this.autoTranslationRadio = autoTranslationRadio;
    }

    public String getTranslationAppComboBox() {
        return translationAppComboBox;
    }

    public void setTranslationAppComboBox(String translationAppComboBox) {
        this.translationAppComboBox = translationAppComboBox;
    }

    public String getAppIdText() {
        return appIdText;
    }

    public void setAppIdText(String appIdText) {
        this.appIdText = appIdText;
    }

    public String getSecretText() {
        return secretText;
    }

    public void setSecretText(String secretText) {
        this.secretText = secretText;
    }

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTableAnnotation() {
        return tableAnnotation;
    }

    public void setTableAnnotation(String tableAnnotation) {
        this.tableAnnotation = tableAnnotation;
    }

    public String getIdAnnotation() {
        return idAnnotation;
    }

    public void setIdAnnotation(String idAnnotation) {
        this.idAnnotation = idAnnotation;
    }

    public String getTableAnnotationProperty() {
        return tableAnnotationProperty;
    }

    public void setTableAnnotationProperty(String tableAnnotationProperty) {
        this.tableAnnotationProperty = tableAnnotationProperty;
    }

    public String getCommentAnnotation() {
        return commentAnnotation;
    }

    public void setCommentAnnotation(String commentAnnotation) {
        this.commentAnnotation = commentAnnotation;
    }

    public String getIntType() {
        return intType;
    }

    public void setIntType(String intType) {
        this.intType = intType;
    }

    public String getLongType() {
        return longType;
    }

    public void setLongType(String longType) {
        this.longType = longType;
    }

    public String getStringType() {
        return stringType;
    }

    public void setStringType(String stringType) {
        this.stringType = stringType;
    }

    public String getBooleanType() {
        return booleanType;
    }

    public void setBooleanType(String booleanType) {
        this.booleanType = booleanType;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public String getDoubleType() {
        return doubleType;
    }

    public void setDoubleType(String doubleType) {
        this.doubleType = doubleType;
    }

    public String getFloatType() {
        return floatType;
    }

    public void setFloatType(String floatType) {
        this.floatType = floatType;
    }

    public String getIntDefaultLength() {
        return intDefaultLength;
    }

    public void setIntDefaultLength(String intDefaultLength) {
        this.intDefaultLength = intDefaultLength;
    }

    public String getLongDefaultLength() {
        return longDefaultLength;
    }

    public void setLongDefaultLength(String longDefaultLength) {
        this.longDefaultLength = longDefaultLength;
    }

    public String getStringDefaultLength() {
        return stringDefaultLength;
    }

    public void setStringDefaultLength(String stringDefaultLength) {
        this.stringDefaultLength = stringDefaultLength;
    }

    public String getDoubleDefaultLength() {
        return doubleDefaultLength;
    }

    public void setDoubleDefaultLength(String doubleDefaultLength) {
        this.doubleDefaultLength = doubleDefaultLength;
    }

    public String getFloatDefaultLength() {
        return floatDefaultLength;
    }

    public void setFloatDefaultLength(String floatDefaultLength) {
        this.floatDefaultLength = floatDefaultLength;
    }

    public String getBooleanDefaultLength() {
        return booleanDefaultLength;
    }

    public void setBooleanDefaultLength(String booleanDefaultLength) {
        this.booleanDefaultLength = booleanDefaultLength;
    }

    public String getDateDefaultLength() {
        return dateDefaultLength;
    }

    public void setDateDefaultLength(String dateDefaultLength) {
        this.dateDefaultLength = dateDefaultLength;
    }

    public String getBigDecimalType() {
        return bigDecimalType;
    }

    public void setBigDecimalType(String bigDecimalType) {
        this.bigDecimalType = bigDecimalType;
    }

    public String getBigDecimalDefaultLength() {
        return bigDecimalDefaultLength;
    }

    public void setBigDecimalDefaultLength(String bigDecimalDefaultLength) {
        this.bigDecimalDefaultLength = bigDecimalDefaultLength;
    }

    public String getLocalDateType() {
        return localDateType;
    }

    public void setLocalDateType(String localDateType) {
        this.localDateType = localDateType;
    }

    public String getLocalTimeType() {
        return localTimeType;
    }

    public void setLocalTimeType(String localTimeType) {
        this.localTimeType = localTimeType;
    }

    public String getLocalDateTimeType() {
        return localDateTimeType;
    }

    public void setLocalDateTimeType(String localDateTimeType) {
        this.localDateTimeType = localDateTimeType;
    }

    public String getLocalDateDefaultLength() {
        return localDateDefaultLength;
    }

    public void setLocalDateDefaultLength(String localDateDefaultLength) {
        this.localDateDefaultLength = localDateDefaultLength;
    }

    public String getLocalTimeDefaultLength() {
        return localTimeDefaultLength;
    }

    public void setLocalTimeDefaultLength(String localTimeDefaultLength) {
        this.localTimeDefaultLength = localTimeDefaultLength;
    }

    public String getLocalDateTimeDefaultLength() {
        return localDateTimeDefaultLength;
    }

    public void setLocalDateTimeDefaultLength(String localDateTimeDefaultLength) {
        this.localDateTimeDefaultLength = localDateTimeDefaultLength;
    }

    public Boolean getShowNoInMapFieldRadio() {
        return showNoInMapFieldRadio;
    }

    public void setShowNoInMapFieldRadio(Boolean showNoInMapFieldRadio) {
        this.showNoInMapFieldRadio = showNoInMapFieldRadio;
    }
}
