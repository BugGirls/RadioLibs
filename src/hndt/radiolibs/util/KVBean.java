package hndt.radiolibs.util;

/**
 * Created by pysh on 2015-03-13.
 */
public class KVBean {
    String code;
    String value;

    public KVBean(String code, String value) {
        this.code = code;
        this.value = value;
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
}
