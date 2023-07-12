package io.devpl.codegen.utils;

import java.util.List;

public class TaskVersionLog {

    String superId;
    String name;
    List<TaskVersionLog> list;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSuperId() {
        return superId;
    }

    public void setSuperId(String superId) {
        this.superId = superId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TaskVersionLog> getList() {
        return list;
    }

    public void setList(List<TaskVersionLog> list) {
        this.list = list;
    }
}
