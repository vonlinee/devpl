package org.apache.ddlutils.platform;

import org.apache.ddlutils.TestBase;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.DatabaseMetaData;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the utility methods in the {@link DatabaseMetaDataWrapper} class.
 */
public class TestDatabaseMetaDataWrapper extends TestBase {
    /**
     * Helper method to create a proxied DatabaseMetaData instance using the given invocation handler.
     *
     * @param handler The handler
     * @return The proxy object
     */
    private DatabaseMetaData createMockDatabaseMetaData(final InvocationHandler handler) {
        return (DatabaseMetaData) Proxy.newProxyInstance(getClass().getClassLoader(),
            new Class[]{DatabaseMetaData.class},
            handler);
    }

    /**
     * Tests the {@link DatabaseMetaDataWrapper#escapeForSearch(String)} method (see DDLUTILS-246).
     */
    @Test
    public void testEscapeSearchString() throws Exception {
        DatabaseMetaData metaData = createMockDatabaseMetaData(new InvocationHandler() {
            /**
             * {@inheritDoc}
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if ("getSearchStringEscape".equals(method.getName())) {
                    return "\\";
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        });

        DatabaseMetaDataWrapper wrapper = new DatabaseMetaDataWrapper();

        wrapper.setMetaData(metaData);

        assertEquals("FOOMATIC", wrapper.escapeForSearch("FOOMATIC"));
        assertEquals("FOO\\_MATIC", wrapper.escapeForSearch("FOO_MATIC"));
        assertEquals("FOO\\%MATIC", wrapper.escapeForSearch("FOO%MATIC"));
    }
}
