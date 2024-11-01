package org.apache.ddlutils.platform;

/**
 * Platform implementation that makes the base functionality available without
 * overriding anything.
 */
public class TestPlatform extends PlatformImplBase {
    /**
     * Creates a new test platform instance.
     */
    public TestPlatform() {
        setSqlBuilder(new SqlBuilder(this) {
        });
    }

    @Override
    public DatabaseType getDBType() {
        return new DatabaseType() {
            @Override
            public String getName() {
                return "TestDB";
            }

            @Override
            public void registerDriverType(DriverType driverType) {

            }

            @Override
            public void deregisterDriverType(DriverType driverType) {

            }

            @Override
            public DriverType[] getSupportedDriverTypes() {
                return new DriverType[0];
            }
        };
    }
}
