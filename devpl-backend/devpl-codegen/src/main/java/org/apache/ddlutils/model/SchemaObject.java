package org.apache.ddlutils.model;

import java.io.Serializable;

public class SchemaObject implements Serializable {

    private String _name;

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public boolean hasName() {
        return _name != null && !_name.isEmpty();
    }
}
