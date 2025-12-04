package com.inventario;

import org.hibernate.dialect.Dialect;

public class SQLiteDialect extends Dialect {

    @Override
    public boolean dropConstraints() {
        return false;
    }

    public boolean supportsIdentityColumns() {
        return true;
    }

    public boolean hasDataTypeInIdentityColumn() {
        return false;
    }

    public String getIdentityColumnString(int type) {
        return "integer";
    }

    public String getIdentitySelectString() {
        return "select last_insert_rowid()";
    }
}
