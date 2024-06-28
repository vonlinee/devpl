package org.apache.ddlutils;

import junit.framework.TestSuite;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ddlutils.io.BinaryObjectsHelper;
import org.apache.ddlutils.io.DataReader;
import org.apache.ddlutils.io.DataToDatabaseSink;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.jdbc.PooledDataSourceWrapper;
import org.apache.ddlutils.model.*;
import org.apache.ddlutils.platform.CreationParameters;
import org.apache.ddlutils.platform.DBTypeEnum;
import org.apache.ddlutils.platform.DefaultValueHelper;
import org.apache.ddlutils.util.StringUtils;
import org.apache.ddlutils.util.Utils;
import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;

import javax.sql.DataSource;
import java.io.*;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Base class tests that are executed against a live database.
 */
public abstract class TestAgainstLiveDatabaseBase extends TestPlatformBase {
    /**
     * The name of the property that specifies properties file with the settings for the connection to test against.
     */
    public static final String JDBC_PROPERTIES_PROPERTY = "jdbc.properties.file";
    /**
     * The prefix for properties of the datasource.
     */
    public static final String DATASOURCE_PROPERTY_PREFIX = "datasource.";
    /**
     * The prefix for properties for ddlutils.
     */
    public static final String DDLUTILS_PROPERTY_PREFIX = "ddlutils.";
    /**
     * The property for specifying the platform.
     */
    public static final String DDLUTILS_PLATFORM_PROPERTY = DDLUTILS_PROPERTY_PREFIX + "platform";
    /**
     * The property specifying the catalog for the tests.
     */
    public static final String DDLUTILS_CATALOG_PROPERTY = DDLUTILS_PROPERTY_PREFIX + "catalog";
    /**
     * The property specifying the schema for the tests.
     */
    public static final String DDLUTILS_SCHEMA_PROPERTY = DDLUTILS_PROPERTY_PREFIX + "schema";
    /**
     * The prefix for table creation properties.
     */
    public static final String DDLUTILS_TABLE_CREATION_PREFIX = DDLUTILS_PROPERTY_PREFIX + "tableCreation.";
    /**
     * The test properties as defined by an external properties file.
     */
    private Properties _testProps;
    /**
     * The data source to test against.
     */
    private PooledDataSourceWrapper _dataSource;
    /**
     * The database name.
     */
    private String _databaseName;
    /**
     * The database model.
     */
    private Database _model;
    /**
     * Whether to use delimited identifiers for the test.
     */
    private boolean _useDelimitedIdentifiers;

    /**
     * Creates the test suite for the given test class which must be a subclass of
     * RoundtripTestBase. If the platform supports it, it will be tested
     * with both delimited and unlimited identifiers.
     *
     * @param testedClass The tested class
     * @return The tests
     */
    protected static TestSuite getTests(Class<?> testedClass) {
        if (!TestAgainstLiveDatabaseBase.class.isAssignableFrom(testedClass) || Modifier.isAbstract(testedClass.getModifiers())) {
            throw new DdlUtilsException("Cannot create parameterized tests for class " + testedClass.getName());
        }

        TestSuite suite = new TestSuite();
        Properties props = readTestProperties();
        if (props == null) {
            return suite;
        }
        PooledDataSourceWrapper dataSource = initDataSourceFromProperties(props);
        String databaseName = determineDatabaseName(props, dataSource);

        try {
            Method[] methods = testedClass.getMethods();
            PlatformInfo info = null;
            TestAgainstLiveDatabaseBase newTest;

            for (Method method : methods) {
                if (method.getName().startsWith("test") && method.getParameterTypes().length == 0) {
                    newTest = (TestAgainstLiveDatabaseBase) newInstance(testedClass);
                    newTest.setName(method.getName());
                    newTest.setTestProperties(props);
                    newTest.setDataSource(dataSource);
                    newTest.setDatabaseName(databaseName);
                    newTest.setUseDelimitedIdentifiers(false);

                    if (info == null) {
                        info = PlatformFactory.createNewPlatformInstance(newTest.getDatabaseName()).getPlatformInfo();
                    }
                    if (info.isDelimitedIdentifiersSupported()) {

                        newTest = (TestAgainstLiveDatabaseBase) newInstance(testedClass);
                        newTest.setName(method.getName());
                        newTest.setTestProperties(props);
                        newTest.setDataSource(dataSource);
                        newTest.setDatabaseName(databaseName);
                        newTest.setUseDelimitedIdentifiers(true);
                    }
                }
            }
        } catch (Exception ex) {
            throw new DdlUtilsException(ex);
        }
        return suite;
    }

    public static DataSource getDataSourceFromProperties() {
        Properties props = readTestProperties();
        if (props == null) {
            return null;
        }
        return initDataSourceFromProperties(props);
    }

    private static <T> T newInstance(Class<T> type) {
        try {
            return Utils.newInstance(type);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the test properties as specified by the property.
     *
     * @return The properties or <code>null</code> if no properties have been specified
     */
    protected static Properties readTestProperties() {
        String propFile = System.getProperty(JDBC_PROPERTIES_PROPERTY);
        if (propFile == null) {
            System.err.println("jdbc properties file is null");
            return null;
        }
        try (InputStream propStream = getPropertiesInputStream(propFile)) {
            Properties props = new Properties();
            props.load(propStream);
            return props;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 获取配置文件输入流
     *
     * @param propsFile 配置文件路径
     * @return InputStream
     * @throws FileNotFoundException 文件不存在
     */
    public static InputStream getPropertiesInputStream(String propsFile) throws FileNotFoundException {
        InputStream inputStream = TestAgainstLiveDatabaseBase.class.getClassLoader().getResourceAsStream(propsFile);
        if (inputStream == null) {
            inputStream = new FileInputStream(propsFile);
        }
        return inputStream;
    }

    /**
     * Initializes the test datasource and the platform.
     *
     * @param props The properties to initialize from
     * @return The data source object
     */
    private static PooledDataSourceWrapper initDataSourceFromProperties(Properties props) {
        if (props == null) {
            return null;
        }
        try {
//            String dataSourceClass = props.getProperty(DATASOURCE_PROPERTY_PREFIX + "class", PooledDataSourceWrapper.class.getName());
//            DataSource dataSource = (DataSource) Utils.newInstance(Class.forName(dataSourceClass));

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setUrl((String) props.get("datasource.url"));
            dataSource.setUsername((String) props.get("datasource.username"));
            dataSource.setPassword((String) props.get("datasource.password"));

//            for (Map.Entry<Object, Object> entry : props.entrySet()) {
//                String propName = (String) entry.getKey();
//                if (propName.startsWith(DATASOURCE_PROPERTY_PREFIX) && !propName.equals(DATASOURCE_PROPERTY_PREFIX + "class")) {
//                    BeanUtils.setProperty(dataSource, propName.substring(DATASOURCE_PROPERTY_PREFIX.length()), entry.getValue());
//                }
//            }
            return new PooledDataSourceWrapper(dataSource);
        } catch (Exception ex) {
            throw new DatabaseOperationException(ex);
        }
    }

    /**
     * Determines the name of the platform to use from the properties or the data source if no
     * platform is specified in the properties.
     *
     * @param props      The test properties
     * @param dataSource The data source
     * @return The name of the platform
     */
    protected static String determineDatabaseName(Properties props, DataSource dataSource) {
        String platformName = props.getProperty(DDLUTILS_PLATFORM_PROPERTY);
        if (platformName == null) {
            // property not set, then try to determine
            platformName = PlatformUtils.determineDatabaseType(dataSource);
            if (platformName == null) {
                throw new DatabaseOperationException("Could not determine platform from datasource, please specify it in the jdbc.properties via the ddlutils.platform property");
            }
        }
        return platformName;
    }

    /**
     * 初始化数据源
     * 子类需实现
     */
    public void initDataSource() throws Exception {
        Properties props = readTestProperties();
        if (props == null) {
            String propFile = System.getProperty(JDBC_PROPERTIES_PROPERTY);
            if (propFile == null) {
                getLog().error("未指定配置文件位置,无法初始化数据源, 通过System.setProperties(\"" + JDBC_PROPERTIES_PROPERTY + "\"\", \"/path/to/file\")指定");
            } else {
                getLog().error("配置文件[" + propFile + "]不存在, 无法初始化数据源");
            }
            return;
        }
        setTestProperties(props);
        PooledDataSourceWrapper dataSource = initDataSourceFromProperties(props);
        String databaseName = determineDatabaseName(props, dataSource);

        setDatabaseName(databaseName);
        setDataSource(dataSource);

        setUp();
    }

    protected String getPropertiesFileLocation() {
        return null;
    }

    /**
     * 初始化数据源
     */
    public void init() {
        Properties props = readTestProperties();

        if (props == null) {
            return;
        }
        PooledDataSourceWrapper dataSource = initDataSourceFromProperties(props);
        String databaseName = determineDatabaseName(props, dataSource);
        setDataSource(dataSource);
        setDatabaseName(databaseName);

        try {
            setUp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the test properties.
     *
     * @return The properties
     */
    protected Properties getTestProperties() {
        return _testProps;
    }

    /**
     * Sets the test properties.
     *
     * @param props The properties
     */
    private void setTestProperties(Properties props) {
        _testProps = props;
    }

    /**
     * Returns the test table creation parameters for the given model.
     *
     * @param model The model
     * @return The creation parameters
     */
    protected CreationParameters getTableCreationParameters(Database model) {
        CreationParameters params = new CreationParameters();
        if (_testProps != null) {
            for (Map.Entry<Object, Object> entry : _testProps.entrySet()) {
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (name.startsWith(DDLUTILS_TABLE_CREATION_PREFIX)) {
                    name = name.substring(DDLUTILS_TABLE_CREATION_PREFIX.length());
                    for (int tableIdx = 0; tableIdx < model.getTableCount(); tableIdx++) {
                        params.addParameter(model.getTable(tableIdx), name, value);
                    }
                }
            }
        }
        return params;
    }

    /**
     * Determines whether the test shall use delimited identifiers.
     *
     * @return Whether to use delimited identifiers
     */
    protected boolean isUseDelimitedIdentifiers() {
        return _useDelimitedIdentifiers;
    }

    /**
     * Specifies whether the test shall use delimited identifiers.
     *
     * @param useDelimitedIdentifiers Whether to use delimited identifiers
     */
    protected void setUseDelimitedIdentifiers(boolean useDelimitedIdentifiers) {
        _useDelimitedIdentifiers = useDelimitedIdentifiers;
    }

    /**
     * Returns the data source.
     *
     * @return The data source
     */
    protected PooledDataSourceWrapper getDataSource() {
        return _dataSource;
    }

    /**
     * Sets the data source.
     *
     * @param dataSource The data source
     */
    private void setDataSource(PooledDataSourceWrapper dataSource) {
        _dataSource = dataSource;
    }


    @Override
    protected String getDatabaseName() {
        return _databaseName;
    }

    /**
     * Sets the database name.
     *
     * @param databaseName The name of the database
     */
    private void setDatabaseName(String databaseName) {
        _databaseName = databaseName;
    }

    /**
     * Returns the database model.
     *
     * @return The model
     */
    protected Database getModel() {
        return _model;
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        getPlatform().setDataSource(getDataSource());
        getPlatform().setDelimitedIdentifierModeOn(_useDelimitedIdentifiers);
    }


    @Override
    protected void tearDown() throws Exception {
        try {
            if (_model != null) {
                dropDatabase();
                _model = null;
            }
        } finally {
            assertAndEnsureClearDatabase();
            super.tearDown();
        }
    }

    /**
     * Creates a new database from the given XML database schema.
     *
     * @param schemaXml The XML database schema
     * @return The parsed database model
     */
    protected Database createDatabase(String schemaXml) throws DatabaseOperationException {
        Database model = parseDatabaseFromString(schemaXml);
        createDatabase(model);
        return model;
    }

    /**
     * Creates a new database from the given model.
     *
     * @param model The model
     */
    protected void createDatabase(Database model) throws DatabaseOperationException {
        try {
            _model = model;
            getPlatform().setSqlCommentsOn(false);
            getPlatform().createDatabase(_model, getTableCreationParameters(_model), true, false);
        } catch (Exception ex) {
            throw new DatabaseOperationException(ex);
        }
    }

    /**
     * Alters the database to match the given model.
     *
     * @param schemaXml The model XML
     * @return The model object
     */
    protected Database alterDatabase(String schemaXml) throws DatabaseOperationException {
        Database model = parseDatabaseFromString(schemaXml);

        alterDatabase(model);
        return model;
    }

    /**
     * Alters the database to match the given model.
     *
     * @param desiredModel The model
     */
    protected void alterDatabase(Database desiredModel) throws DatabaseOperationException {
        try {
            _model = desiredModel;
            _model.resetTableModelCache();

            Database liveModel = readModelFromDatabase(desiredModel.getName());

            getPlatform().setSqlCommentsOn(false);
            getPlatform().alterModel(liveModel, _model, getTableCreationParameters(_model), false);
        } catch (Exception ex) {
            throw new DatabaseOperationException(ex);
        }
    }

    /**
     * Inserts data into the database.
     *
     * @param dataXml The data xml
     * @return The database
     */
    protected Database insertData(String dataXml) throws DatabaseOperationException {
        try {
            DataReader dataReader = new DataReader();
            dataReader.setModel(_model);
            dataReader.setSink(new DataToDatabaseSink(getPlatform(), _model));
            dataReader.sink(dataXml);
            return _model;
        } catch (Exception ex) {
            throw new DatabaseOperationException(ex);
        }
    }

    /**
     * Drops the tables defined in the database model.
     */
    protected void dropDatabase() throws DatabaseOperationException {
        getPlatform().dropModel(_model, true);
    }

    /**
     * Inserts a row into the designated table.
     *
     * @param tableName    The name of the table (case-insensitive)
     * @param columnValues The values for the columns in order of definition
     * @return The dyna bean for the row
     */
    protected TableRow insertRow(String tableName, Object[] columnValues) {
        Table table = getModel().findTable(tableName);
        TableRow bean = getModel().createTableRow(table);
        for (int idx = 0; (idx < table.getColumnCount()) && (idx < columnValues.length); idx++) {
            Column column = table.getColumn(idx);
            bean.setColumnValue(column.getName(), columnValues[idx]);
        }
        getPlatform().insert(getModel(), bean);
        return bean;
    }

    /**
     * Updates the row in the designated table.
     *
     * @param tableName    The name of the table (case-insensitive)
     * @param oldBean      The bean representing the current row
     * @param columnValues The values for the columns in order of definition
     * @return The dyna bean for the new row
     */
    protected TableRow updateRow(String tableName, TableRow oldBean, Object[] columnValues) {
        Table table = getModel().findTable(tableName);
        TableRow bean = getModel().createTableRow(table);

        for (int idx = 0; (idx < table.getColumnCount()) && (idx < columnValues.length); idx++) {
            Column column = table.getColumn(idx);

            bean.setColumnValue(column.getName(), columnValues[idx]);
        }
        getPlatform().update(getModel(), oldBean, bean);
        return bean;
    }

    /**
     * Deletes the specified row from the table.
     *
     * @param tableName      The name of the table (case-insensitive)
     * @param pkColumnValues The values for the pk columns in order of definition
     */
    protected void deleteRow(String tableName, Object[] pkColumnValues) {
        Table table = getModel().findTable(tableName);
        TableRow bean = getModel().createTableRow(table);
        Column[] pkColumns = table.getPrimaryKeyColumns();

        for (int idx = 0; (idx < pkColumns.length) && (idx < pkColumnValues.length); idx++) {
            bean.setColumnValue(pkColumns[idx].getName(), pkColumnValues[idx]);
        }
        getPlatform().delete(getModel(), bean);
    }

    /**
     * Returns a "SELECT * FROM [table name]" statement. It also takes
     * delimited identifier mode into account if enabled.
     *
     * @param table       The table
     * @param orderColumn The column to order the rows by (can be <code>null</code>)
     * @return The statement
     */
    protected String getSelectQueryForAllString(Table table, String orderColumn) {
        StringBuilder query = new StringBuilder();

        query.append("SELECT * FROM ");
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            query.append(getPlatformInfo().getDelimiterToken());
        }
        query.append(table.getName());
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            query.append(getPlatformInfo().getDelimiterToken());
        }
        if (orderColumn != null) {
            query.append(" ORDER BY ");
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                query.append(getPlatformInfo().getDelimiterToken());
            }
            query.append(orderColumn);
            if (getPlatform().isDelimitedIdentifierModeOn()) {
                query.append(getPlatformInfo().getDelimiterToken());
            }
        }
        return query.toString();
    }

    /**
     * Retrieves all rows from the given table.
     *
     * @param tableName The table
     * @return The rows
     */
    protected List<TableRow> getRows(String tableName) {
        Table table = getModel().findTable(tableName, getPlatform().isDelimitedIdentifierModeOn());
        return getPlatform().fetch(getModel(), getSelectQueryForAllString(table, null), new Table[]{table});
    }

    /**
     * Retrieves all rows from the given table.
     *
     * @param tableName   The table
     * @param orderColumn The column to order the rows by
     * @return The rows
     */
    protected List<TableRow> getRows(String tableName, String orderColumn) {
        Table table = getModel().findTable(tableName, getPlatform().isDelimitedIdentifierModeOn());
        return getPlatform().fetch(getModel(), getSelectQueryForAllString(table, orderColumn), new Table[]{table});
    }

    /**
     * Checks that the database is clear, and if not clears it (no tables, sequences etc. left) and
     * throws an {@link AssertionFailedError}.
     */
    protected void assertAndEnsureClearDatabase() {
        Database liveModel = readModelFromDatabase("tmp");
        boolean hasStuff = false;

        if (liveModel.getTableCount() > 0) {
            hasStuff = true;
            try {
                getPlatform().dropModel(liveModel, true);
            } catch (Exception ex) {
                getLog().error("Could not clear database", ex);
            }
        }
        if (DBTypeEnum.FIREBIRD.getName().equals(getPlatform().getName()) || DBTypeEnum.INTERBASE.getName().equals(getPlatform().getName())) {
            Connection connection = null;

            try {
                connection = getPlatform().borrowConnection();

                hasStuff = hasStuff | dropTriggers(connection);
                hasStuff = hasStuff | dropGenerators(connection);
            } catch (Exception ex) {
                getLog().error("Could not clear database", ex);
            } finally {
                getPlatform().returnConnection(connection);
            }
        }
        // TODO: Check for sequences
        if (hasStuff) {
            Assertions.fail("Database is not empty after test");
        }
    }

    /**
     * Drops generators left by a test in a Firebird/Interbase database.
     *
     * @param connection The database connection
     * @return Whether generators were dropped
     */
    private boolean dropGenerators(Connection connection) {
        Statement stmt = null;
        boolean hasGenerators = false;

        try {
            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT RDB$GENERATOR_NAME FROM RDB$GENERATORS WHERE RDB$GENERATOR_NAME NOT LIKE '%$%'");
            List<String> names = new ArrayList<>();

            while (rs.next()) {
                names.add(rs.getString(1));
            }
            rs.close();

            for (String name : names) {
                if (name.toLowerCase().startsWith("gen_")) {
                    hasGenerators = true;
                    stmt.execute("DROP GENERATOR " + name);
                }
            }
        } catch (Exception ex) {
            getLog().error("Error while dropping the remaining generators", ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    getLog().error("Error while clearing the database", ex);
                }
            }
        }
        return hasGenerators;
    }

    /**
     * Drops triggers left by a test in a Firebird/Interbase database.
     *
     * @param connection The database connection
     * @return Whether triggers were dropped
     */
    private boolean dropTriggers(Connection connection) {
        Statement stmt = null;
        boolean hasTriggers = false;

        try {
            stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM RDB$TRIGGERS WHERE RDB$SYSTEM_FLAG IS NULL OR RDB$SYSTEM_FLAG = 0");
            List<String> names = new ArrayList<>();

            while (rs.next()) {
                names.add(rs.getString(1));
            }
            rs.close();

            for (String name : names) {
                if (name.toLowerCase().startsWith("trg_")) {
                    hasTriggers = true;
                    stmt.execute("DROP TRIGGER " + name);
                }
            }
        } catch (Exception ex) {
            getLog().error("Error while dropping the remaining triggers", ex);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception ex) {
                    getLog().error("Error while clearing the database", ex);
                }
            }
        }
        return hasTriggers;
    }

    /**
     * Reads the database model from a live database.
     *
     * @param databaseName The name of the resulting database
     * @return The model
     */
    protected Database readModelFromDatabase(String databaseName) {
        Properties props = getTestProperties();
        String catalog = props.getProperty(DDLUTILS_CATALOG_PROPERTY);
        String schema = props.getProperty(DDLUTILS_SCHEMA_PROPERTY);

        return getPlatform().readModelFromDatabase(databaseName, catalog, schema, null);
    }

    /**
     * Returns a copy of the given model adjusted for type changes because of the native type mappings
     * which when read back from the database will map to different types.
     *
     * @param sourceModel The source model
     * @return The adjusted model
     */
    protected Database adjustModel(Database sourceModel) {
        Database model = new DefaultModelCopier().copy(sourceModel);

        for (int tableIdx = 0; tableIdx < model.getTableCount(); tableIdx++) {
            Table table = model.getTable(tableIdx);

            for (int columnIdx = 0; columnIdx < table.getColumnCount(); columnIdx++) {
                Column column = table.getColumn(columnIdx);
                int origType = column.getTypeCode();
                int targetType = getPlatformInfo().getTargetJdbcType(origType);

                // we adjust the column types if the native type would back-map to a
                // different jdbc type
                if (targetType != origType) {
                    column.setTypeCode(targetType);
                    // we should also adapt the default value
                    if (column.getDefaultValue() != null) {
                        DefaultValueHelper helper = getPlatform().getSqlBuilder().getDefaultValueHelper();

                        column.setDefaultValue(helper.convert(column.getDefaultValue(), origType, targetType));
                    }
                }
                // we also promote the default size if the column has no size
                // spec of its own
                if ((column.getSize() == null) && getPlatformInfo().hasSize(targetType)) {
                    Integer defaultSize = getPlatformInfo().getDefaultSize(targetType);

                    if (defaultSize != null) {
                        column.setSize(defaultSize.toString());
                    }
                }
                // finally the platform might return a synthetic default value if the column
                // is a primary key column
                if (getPlatformInfo().isSyntheticDefaultValueForRequiredReturned() && (column.getDefaultValue() == null) && column.isRequired() && !column.isAutoIncrement()) {
                    switch (column.getTypeCode()) {
                        case Types.TINYINT, Types.SMALLINT, Types.INTEGER, Types.BIGINT -> column.setDefaultValue("0");
                        case Types.REAL, Types.FLOAT, Types.DOUBLE -> column.setDefaultValue("0.0");
                        case Types.BIT -> column.setDefaultValue("false");
                        default -> column.setDefaultValue("");
                    }
                }
                if (column.isPrimaryKey() && getPlatformInfo().isPrimaryKeyColumnAutomaticallyRequired()) {
                    column.setRequired(true);
                }
                if (column.isAutoIncrement() && getPlatformInfo().isIdentityColumnAutomaticallyRequired()) {
                    column.setRequired(true);
                }
            }
            // we also add the default names to foreign keys that are initially unnamed
            for (int fkIdx = 0; fkIdx < table.getForeignKeyCount(); fkIdx++) {
                ForeignKey fk = table.getForeignKey(fkIdx);

                if (fk.getName() == null) {
                    fk.setName(getPlatform().getSqlBuilder().getForeignKeyName(table, fk));
                }
            }
        }
        return model;
    }

    /**
     * Returns the original model adjusted for type changes because of the native type mappings
     * which when read back from the database will map to different types.
     *
     * @return The adjusted model
     */
    protected Database getAdjustedModel() {
        Database model = getModel();

        return model == null ? null : adjustModel(model);
    }

    /**
     * Returns the SQL for altering the live database so that it matches the given model.
     *
     * @param desiredModel The desired model
     * @return The alteration SQL
     */
    protected String getAlterTablesSql(Database desiredModel) {
        Database liveModel = readModelFromDatabase(desiredModel.getName());

        return getPlatform().getAlterModelSql(liveModel, desiredModel, getTableCreationParameters(desiredModel));
    }

    /**
     * Determines the value of the bean's property that has the given name. Depending on the
     * case-setting of the current builder, the case of teh name is considered or not.
     *
     * @param bean     The bean
     * @param propName The name of the property
     * @return The value
     */
    protected Object getPropertyValue(TableRow bean, String propName) {
        if (getPlatform().isDelimitedIdentifierModeOn()) {
            return bean.getColumnValue(propName);
        } else {
            ColumnProperty[] props = bean.getTableModel().getOriginProperties();

            for (ColumnProperty prop : props) {
                if (propName.equalsIgnoreCase(prop.getName())) {
                    return bean.getColumnValue(prop.getName());
                }
            }
            throw new IllegalArgumentException("The bean has no property with the name " + propName);
        }
    }

    /**
     * Asserts that the two given database models are equal, and if not, writes both of them
     * in XML form to <code>stderr</code>.
     *
     * @param expected The expected model
     * @param actual   The actual model
     */
    protected void assertEquals(Database expected, Database actual) {
        assertEquals(expected, actual, _useDelimitedIdentifiers);
    }

    protected void assertEquals(String message, Object expected, Object actual) {
        Assertions.assertEquals(expected, actual, message);
    }

    /**
     * Asserts that the two given database tables are equal.
     *
     * @param expected The expected table
     * @param actual   The actual table
     */
    protected void assertEquals(Table expected, Table actual) {
        assertEquals(expected, actual, _useDelimitedIdentifiers);
    }

    /**
     * Asserts that the two given columns are equal.
     *
     * @param expected The expected column
     * @param actual   The actual column
     */
    protected void assertEquals(Column expected, Column actual) {
        assertEquals(expected, actual, _useDelimitedIdentifiers);
    }

    protected void assertEquals(Object expected, Object actual) {

    }

    /**
     * Asserts that the two given foreign keys are equal.
     *
     * @param expected The expected foreign key
     * @param actual   The actual foreign key
     */
    protected void assertEquals(ForeignKey expected, ForeignKey actual) {
        assertEquals(expected, actual, _useDelimitedIdentifiers);
    }

    /**
     * Asserts that the two given references are equal.
     *
     * @param expected The expected reference
     * @param actual   The actual reference
     */
    protected void assertEquals(Reference expected, Reference actual) {
        assertEquals(expected, actual, _useDelimitedIdentifiers);
    }

    /**
     * Asserts that the two given indices are equal.
     *
     * @param expected The expected index
     * @param actual   The actual index
     */
    protected void assertEquals(Index expected, Index actual) {
        assertEquals(expected, actual, _useDelimitedIdentifiers);
    }

    /**
     * Asserts that the two given index columns are equal.
     *
     * @param expected The expected index column
     * @param actual   The actual index column
     */
    protected void assertEquals(IndexColumn expected, IndexColumn actual) {
        assertEquals(expected, actual, _useDelimitedIdentifiers);
    }

    /**
     * Compares the specified attribute value of the given bean with the expected object.
     *
     * @param expected The expected object
     * @param bean     The bean
     * @param attrName The attribute name
     */
    protected void assertEquals(Object expected, Object bean, String attrName) {
        TableRow dynaBean = (TableRow) bean;
        Object value = dynaBean.getColumnValue(attrName);

        if (value instanceof byte[] && !(expected instanceof byte[])) {
            TableModel dynaClass = dynaBean.getTableModel();
            Column column = dynaClass.getProperty(attrName).getColumn();

            if (TypeMap.isBinaryType(column.getTypeCode())) {
                value = new BinaryObjectsHelper().deserialize((byte[]) value);
            }
        }
        if (expected == null) {
            Assertions.assertNull(value);
        } else {
            Assertions.assertEquals(expected, value);
        }
    }

    /**
     * Asserts that the two given database models are equal, and if not, writes both of them
     * in XML form to <code>stderr</code>.
     *
     * @param expected      The expected model
     * @param actual        The actual model
     * @param caseSensitive Whether case matters when comparing
     */
    protected void assertEquals(Database expected, Database actual, boolean caseSensitive) {
        try {
            Assertions.assertEquals(expected.getName(), actual.getName(), "Model names do not match.");
            Assertions.assertEquals(expected.getTableCount(), actual.getTableCount(), "Not the same number of tables.");
            for (int tableIdx = 0; tableIdx < actual.getTableCount(); tableIdx++) {
                assertEquals(expected.getTable(tableIdx), actual.getTable(tableIdx), caseSensitive);
            }
        } catch (Throwable ex) {
            StringWriter writer = new StringWriter();
            DatabaseIO dbIo = new DatabaseIO();

            dbIo.write(expected, writer);

            getLog().error("Expected model:\n" + writer);

            writer = new StringWriter();
            dbIo.write(actual, writer);

            getLog().error("Actual model:\n" + writer);

            if (ex instanceof Error) {
                throw (Error) ex;
            } else {
                throw new DdlUtilsException(ex);
            }
        }
    }

    /**
     * Asserts that the two given database tables are equal.
     *
     * @param expected      The expected table
     * @param actual        The actual table
     * @param caseSensitive Whether case matters when comparing
     */
    protected void assertEquals(Table expected, Table actual, boolean caseSensitive) {
        if (caseSensitive) {
            Assertions.assertEquals("Table names do not match.", getPlatform().getSqlBuilder().shortenName(expected.getName(), getSqlBuilder().getMaxTableNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName(), getSqlBuilder().getMaxTableNameLength()));
        } else {
            Assertions.assertEquals("Table names do not match (ignoring case).", getPlatform().getSqlBuilder().shortenName(expected.getName().toUpperCase(), getSqlBuilder().getMaxTableNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName().toUpperCase(), getSqlBuilder().getMaxTableNameLength()));
        }
        Assertions.assertEquals(expected.getColumnCount(), actual.getColumnCount(), "Not the same number of columns in table " + actual.getName() + ".");
        for (int columnIdx = 0; columnIdx < actual.getColumnCount(); columnIdx++) {
            assertEquals(expected.getColumn(columnIdx), actual.getColumn(columnIdx), caseSensitive);
        }
        Assertions.assertEquals(expected.getForeignKeyCount(), actual.getForeignKeyCount(), "Not the same number of foreign keys in table " + actual.getName() + ".");
        // order is not assumed with the way foreign keys are returned.
        for (int expectedFkIdx = 0; expectedFkIdx < expected.getForeignKeyCount(); expectedFkIdx++) {
            ForeignKey expectedFk = expected.getForeignKey(expectedFkIdx);
            String expectedName = getPlatform().getSqlBuilder().shortenName(expectedFk.getName(), getSqlBuilder().getMaxForeignKeyNameLength());
            for (int actualFkIdx = 0; actualFkIdx < actual.getForeignKeyCount(); actualFkIdx++) {
                ForeignKey actualFk = actual.getForeignKey(actualFkIdx);
                String actualName = getPlatform().getSqlBuilder().shortenName(actualFk.getName(), getSqlBuilder().getMaxForeignKeyNameLength());

                if (StringUtils.equals(expectedName, actualName, caseSensitive)) {
                    assertEquals(expectedFk, actualFk, caseSensitive);
                }
            }
        }
        Assertions.assertEquals(expected.getIndexCount(), actual.getIndexCount(), "Not the same number of indices in table " + actual.getName() + ".");
        for (int indexIdx = 0; indexIdx < actual.getIndexCount(); indexIdx++) {
            assertEquals(expected.getIndex(indexIdx), actual.getIndex(indexIdx), caseSensitive);
        }
    }

    /**
     * Asserts that the two given columns are equal.
     *
     * @param expected      The expected column
     * @param actual        The actual column
     * @param caseSensitive Whether case matters when comparing
     */
    protected void assertEquals(Column expected, Column actual, boolean caseSensitive) {
        if (caseSensitive) {
            Assertions.assertEquals("Column names do not match.", getPlatform().getSqlBuilder().shortenName(expected.getName(), getSqlBuilder().getMaxColumnNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName(), getSqlBuilder().getMaxColumnNameLength()));
        } else {
            Assertions.assertEquals("Column names do not match (ignoring case).", getPlatform().getSqlBuilder().shortenName(expected.getName().toUpperCase(), getSqlBuilder().getMaxColumnNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName().toUpperCase(), getSqlBuilder().getMaxColumnNameLength()));
        }
        Assertions.assertEquals(expected.isPrimaryKey(), actual.isPrimaryKey(), "Primary key status not the same for column " + actual.getName() + ".");
        Assertions.assertEquals(expected.isRequired(), actual.isRequired(), "Required status not the same for column " + actual.getName() + ".");
        if (getPlatformInfo().getIdentityStatusReadingSupported()) {
            // we're only comparing this if the platform can actually read the
            // auto-increment status back from an existing database
            assertEquals(expected.isAutoIncrement(), actual.isAutoIncrement(), "Auto-increment status not the same for column " + actual.getName() + ".");
        }
        Assertions.assertEquals("Type not the same for column " + actual.getName() + ".", expected.getType(), actual.getType());
        Assertions.assertEquals(expected.getTypeCode(), actual.getTypeCode(), "Type code not the same for column " + actual.getName() + ".");
        Assertions.assertEquals(expected.getParsedDefaultValue(), actual.getParsedDefaultValue(), "Parsed default values do not match for column " + actual.getName() + ".");

        // comparing the size makes only sense for types where it is relevant
        if ((expected.getTypeCode() == Types.NUMERIC) || (expected.getTypeCode() == Types.DECIMAL)) {
            Assertions.assertEquals(expected.getSizeAsInt(), actual.getSizeAsInt(), "Precision not the same for column " + actual.getName() + ".");
            Assertions.assertEquals(expected.getScale(), actual.getScale(), "Scale not the same for column " + actual.getName() + ".");
        } else if ((expected.getTypeCode() == Types.CHAR) || (expected.getTypeCode() == Types.VARCHAR) || (expected.getTypeCode() == Types.BINARY) || (expected.getTypeCode() == Types.VARBINARY)) {
            Assertions.assertEquals("Size not the same for column " + actual.getName() + ".", expected.getSize(), actual.getSize());
        }
    }

    protected void assertEquals(int a, int b) {
        Assertions.assertEquals(a, b);
    }

    /**
     * Asserts that the two given foreign keys are equal.
     *
     * @param expected      The expected foreign key
     * @param actual        The actual foreign key
     * @param caseSensitive Whether case matters when comparing
     */
    protected void assertEquals(ForeignKey expected, ForeignKey actual, boolean caseSensitive) {
        if (caseSensitive) {
            Assertions.assertEquals("Foreign key names do not match.", getPlatform().getSqlBuilder().shortenName(expected.getName(), getSqlBuilder().getMaxForeignKeyNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName(), getSqlBuilder().getMaxForeignKeyNameLength()));
            Assertions.assertEquals("Referenced table names do not match.", getPlatform().getSqlBuilder().shortenName(expected.getForeignTableName(), getSqlBuilder().getMaxTableNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getForeignTableName(), getSqlBuilder().getMaxTableNameLength()));
        } else {
            Assertions.assertEquals("Foreign key names do not match (ignoring case).", getPlatform().getSqlBuilder().shortenName(expected.getName().toUpperCase(), getSqlBuilder().getMaxForeignKeyNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName().toUpperCase(), getSqlBuilder().getMaxForeignKeyNameLength()));
            Assertions.assertEquals("Referenced table names do not match (ignoring case).", getPlatform().getSqlBuilder().shortenName(expected.getForeignTableName().toUpperCase(), getSqlBuilder().getMaxTableNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getForeignTableName().toUpperCase(), getSqlBuilder().getMaxTableNameLength()));
        }

        Assertions.assertTrue(expected.getOnUpdate().equals(actual.getOnUpdate()) || getPlatformInfo().areEquivalentOnUpdateActions(expected.getOnUpdate(), actual.getOnUpdate()), "Not the same onUpdate setting in foreign key " + actual.getName() + ": expected = " + expected.getOnUpdate() + ", actual = " + actual.getOnUpdate());
        Assertions.assertTrue(expected.getOnDelete().equals(actual.getOnDelete()) || getPlatformInfo().areEquivalentOnDeleteActions(expected.getOnDelete(), actual.getOnDelete()), "Not the same onDelete setting in foreign key " + actual.getName() + ": expected = " + expected.getOnDelete() + ", actual = " + actual.getOnDelete());

        assertEquals(expected.getReferenceCount(), actual.getReferenceCount(), "Not the same number of references in foreign key " + actual.getName() + ".");
        for (int refIdx = 0; refIdx < actual.getReferenceCount(); refIdx++) {
            assertEquals(expected.getReference(refIdx), actual.getReference(refIdx), caseSensitive);
        }
    }

    /**
     * Asserts that the two given references are equal.
     *
     * @param expected      The expected reference
     * @param actual        The actual reference
     * @param caseSensitive Whether case matters when comparing
     */
    protected void assertEquals(Reference expected, Reference actual, boolean caseSensitive) {
        if (caseSensitive) {
            Assertions.assertEquals("Local column names do not match.", getPlatform().getSqlBuilder().shortenName(expected.getLocalColumnName(), getSqlBuilder().getMaxColumnNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getLocalColumnName(), getSqlBuilder().getMaxColumnNameLength()));
            Assertions.assertEquals("Foreign column names do not match.", getPlatform().getSqlBuilder().shortenName(expected.getForeignColumnName(), getSqlBuilder().getMaxColumnNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getForeignColumnName(), getSqlBuilder().getMaxColumnNameLength()));
        } else {
            Assertions.assertEquals("Local column names do not match (ignoring case).", getPlatform().getSqlBuilder().shortenName(expected.getLocalColumnName().toUpperCase(), getSqlBuilder().getMaxColumnNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getLocalColumnName().toUpperCase(), getSqlBuilder().getMaxColumnNameLength()));
            Assertions.assertEquals("Foreign column names do not match (ignoring case).", getPlatform().getSqlBuilder().shortenName(expected.getForeignColumnName().toUpperCase(), getSqlBuilder().getMaxColumnNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getForeignColumnName().toUpperCase(), getSqlBuilder().getMaxColumnNameLength()));
        }
    }

    /**
     * Asserts that the two given indices are equal.
     *
     * @param expected      The expected index
     * @param actual        The actual index
     * @param caseSensitive Whether case matters when comparing
     */
    protected void assertEquals(Index expected, Index actual, boolean caseSensitive) {
        if (caseSensitive) {
            Assertions.assertEquals("Index names do not match.", getPlatform().getSqlBuilder().shortenName(expected.getName(), getSqlBuilder().getMaxConstraintNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName(), getSqlBuilder().getMaxConstraintNameLength()));
        } else {
            Assertions.assertEquals("Index names do not match (ignoring case).", getPlatform().getSqlBuilder().shortenName(expected.getName().toUpperCase(), getSqlBuilder().getMaxConstraintNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName().toUpperCase(), getSqlBuilder().getMaxConstraintNameLength()));
        }
        Assertions.assertEquals(expected.isUnique(), actual.isUnique(), "Unique status not the same for index " + actual.getName() + ".");
        Assertions.assertEquals(expected.getColumnCount(), actual.getColumnCount(), "Not the same number of columns in index " + actual.getName() + ".");
        for (int columnIdx = 0; columnIdx < actual.getColumnCount(); columnIdx++) {
            assertEquals(expected.getColumn(columnIdx), actual.getColumn(columnIdx), caseSensitive);
        }
    }

    /**
     * Asserts that the two given index columns are equal.
     *
     * @param expected      The expected index column
     * @param actual        The actual index column
     * @param caseSensitive Whether case matters when comparing
     */
    protected void assertEquals(IndexColumn expected, IndexColumn actual, boolean caseSensitive) {
        if (caseSensitive) {
            Assertions.assertEquals("Index column names do not match.", getPlatform().getSqlBuilder().shortenName(expected.getName(), getSqlBuilder().getMaxColumnNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName(), getSqlBuilder().getMaxColumnNameLength()));
        } else {
            Assertions.assertEquals("Index column names do not match (ignoring case).", getPlatform().getSqlBuilder().shortenName(expected.getName().toUpperCase(), getSqlBuilder().getMaxColumnNameLength()), getPlatform().getSqlBuilder().shortenName(actual.getName().toUpperCase(), getSqlBuilder().getMaxColumnNameLength()));
        }
        Assertions.assertEquals("Size not the same for index column " + actual.getName() + ".", expected.getSize(), actual.getSize());
    }
}
