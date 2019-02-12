package hndt.radiolibs.biz;

import com.google.gson.reflect.TypeToken;
import hndt.radiolibs.bean.PropertyBean;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.*;

public class PropertyBusiness implements Serializable {

    private static Map<String, String> map;
    private static PropertyBusiness biz;

    private PropertyBusiness() {
        reloadCache();
    }

    public static PropertyBusiness getInstance() {
        if (biz == null) {
            biz = new PropertyBusiness();
        }
        return biz;
    }

    public int save(String code, String name, String value) {
        int r = -1;
        if (Utils.isEmpty(map.get(code))) {
            r = DBTool.update("INSERT property(code,name,value) VALUES(?,?,?)", code, name, value);
            if (r > 0) {
                map.put(code, value);
            }
        } else {
            r = DBTool.update("UPDATE property SET value=? WHERE code=?", value, code);
            if (r > 0) {
                map.remove(code);
                map.put(code, value);
            }
        }
        return r;
    }

    public String find(String code) {
        String value = map.get(code);
        return value;
    }

    public List<PropertyBean> list() {
        List<PropertyBean> list = DBTool.list(PropertyBean.class, "SELECT * FROM property");
        return list;
    }

    public void reloadCache() {
        if (map == null) {
            map = new HashMap<>();
        }
        map.clear();
        List<PropertyBean> linkedList = list();
        for (PropertyBean pbean : linkedList) {
            map.put(pbean.getCode(), pbean.getValue());
        }
    }

}
