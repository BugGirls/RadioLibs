package hndt.radiolibs.util;

import java.io.Serializable;

public class ColumnBean implements Serializable {

    String name;
    String type;
    String comment;

    public ColumnBean(String name, String type, String comment) {
        this.name = name;
        this.type = type;
        this.comment = comment;
        int idx = type.indexOf('(');
        if (idx > 0) {
            this.type = type.substring(0, idx);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
