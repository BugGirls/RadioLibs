package hndt.radiolibs.jsf;

import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value = "integerConverter")
public class IntegerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String str) {
        int d = 0;
        try {
            if (Utils.isNotEmpty(str)) {
                d = Integer.parseInt(str);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return d;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        String str = null;
        if (o != null && o instanceof Integer) {
            str = String.valueOf(o);
        }
        return str;
    }

}
