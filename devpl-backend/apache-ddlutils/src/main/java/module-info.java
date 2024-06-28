module apache.ddlutils {
    requires java.sql;
    requires org.jetbrains.annotations;
    requires org.slf4j;
    requires java.desktop;
    exports org.apache.ddlutils.platform.mysql;
    exports org.apache.ddlutils.jdbc;
    exports org.apache.ddlutils.platform;
    exports org.apache.ddlutils.model;
    exports org.apache.ddlutils.jdbc.meta;
    exports org.apache.ddlutils;
    exports org.apache.ddlutils.alteration;
    exports org.apache.ddlutils.util;
}
