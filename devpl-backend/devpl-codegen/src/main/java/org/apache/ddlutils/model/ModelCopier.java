package org.apache.ddlutils.model;

public interface ModelCopier {

    Database copy(Database source);

    Table copy(Table source, boolean cloneIndexes, boolean cloneForeignKeys, Database targetModel, boolean caseSensitive);

    Column copy(Column source, boolean clonePrimaryKeyStatus);

    Index copy(Index source, Table targetTable, boolean caseSensitive);

    IndexColumn copy(IndexColumn source, Table targetTable, boolean caseSensitive);

    ForeignKey copy(ForeignKey source, Table owningTable, Database targetModel, boolean caseSensitive);

    Reference copy(Reference source, Table localTable, Table foreignTable, boolean caseSensitive);
}
