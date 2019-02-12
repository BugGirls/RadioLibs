package hndt.radiolibs.bean;

import java.io.Serializable;

public class PropertyBean implements Serializable {
    int id;
    String code;
    String name;
    String value;

    public PropertyBean() {
    }

    public PropertyBean(String code, String name, String value) {
        this.code = name;
        this.name = name;
        this.value = value;
    }

    public PropertyBean(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
