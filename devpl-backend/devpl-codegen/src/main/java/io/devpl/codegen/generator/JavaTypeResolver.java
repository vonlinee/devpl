package io.devpl.codegen.generator;

import io.devpl.codegen.type.DataType;

import java.util.List;
import java.util.Properties;

/**
 * This interface describes methods that are required in any Java type resolver.
 * A Java type resolver is used to make a default translation between a JDBC
 * type as returned from the database introspection process, and a Java type.
 */
public interface JavaTypeResolver {
    /**
     * Adds properties for this instance from any properties configured in the
     * JavaTypeResolverConfiguration.
     *
     * <p>This method will be called before any of the get methods.
     *
     * @param properties All properties from the configuration
     */
    void addConfigurationProperties(Properties properties);

    /**
     * Sets the instance of the Context object associated with this instance.
     *
     * <p>This method will be called before any of the get methods.
     *
     * @param context The current Context
     */
    void setContext(Context context);

    /**
     * The generator will supply a list to this method. The implementation class may add strings to the list that will
     * be treated as warning messages and displayed to the user. The concept of a warning is that code generation can
     * continue, but that the results may not be what is expected.
     *
     * @param warnings the new warnings
     */
    void setWarnings(List<String> warnings);

    /**
     * Calculates and returns the Java type that should be associated with this
     * column based on the jdbc type, length, and scale of the column.
     *
     * @param introspectedColumn the column whose Java type needs to be calculated
     * @return the calculated type, or null if an unsupported data type. If null
     * is returned, we will set the type to Object and issue a
     * warning unless the column is ignored or otherwise overridden
     */
    DataType calculateJavaType(ColumnGeneration introspectedColumn);

    /**
     * Calculates and returns the JDBC type name that should be associated with
     * this column based on the jdbc type, length, and scale of the column.
     *
     * @param introspectedColumn the column whose Java type needs to be calculated
     * @return the calculated type name, or null if an unsupported data type. If
     * null is returned, we will set the type to OTHER and issue a
     * warning unless the column is ignored or otherwise overridden
     */
    String calculateJdbcTypeName(ColumnGeneration introspectedColumn);
}
