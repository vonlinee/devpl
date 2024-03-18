package io.devpl.codegen.jdbc.meta;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FunctionMetadata implements Serializable {

    /**
     * FUNCTION_CAT String => function catalog (maybe null)
     **/
    private String functionCatalog;
    /**
     * FUNCTION_SCHEM String => function schema (maybe null)
     **/
    private String functionSchema;
    /**
     * FUNCTION_NAME String => function name. This is the name used to invoke the function
     **/
    private String functionName;
    /**
     * REMARKS String => explanatory comment on the function
     **/
    private String remarks;
    /**
     * FUNCTION_TYPE short => kind of function:
     **/
    private short functionType;
    /**
     * SPECIFIC_NAME String => the name which uniquely identifies this function within its schema. This is a user specified, or DBMS generated, name that may be different then the FUNCTION_NAME for example with overload functions
     **/
    private String specificName;

    public void initialize(ResultSet resultSet) {
        try {
            this.functionCatalog = resultSet.getString(1);
            this.functionSchema = resultSet.getString(2);
            this.functionName = resultSet.getString(3);
            this.remarks = resultSet.getString(4);
            this.functionType = resultSet.getShort(5);
            this.specificName = resultSet.getString(6);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getFunctionCatalog() {
        return functionCatalog;
    }

    public void setFunctionCatalog(String functionCatalog) {
        this.functionCatalog = functionCatalog;
    }

    public String getFunctionSchema() {
        return functionSchema;
    }

    public void setFunctionSchema(String functionSchema) {
        this.functionSchema = functionSchema;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public short getFunctionType() {
        return functionType;
    }

    public void setFunctionType(short functionType) {
        this.functionType = functionType;
    }

    public String getSpecificName() {
        return specificName;
    }

    public void setSpecificName(String specificName) {
        this.specificName = specificName;
    }

    @Override
    public String toString() {
        return "FunctionMetadata{" +
               "functionCat='" + functionCatalog + '\'' +
               ", functionSchem='" + functionSchema + '\'' +
               ", functionName='" + functionName + '\'' +
               ", remarks='" + remarks + '\'' +
               ", functionType=" + functionType +
               ", specificName='" + specificName + '\'' +
               '}';
    }
}
