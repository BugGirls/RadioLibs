package hndt.radiolibs.jsf;

import hndt.radiolibs.util.Utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.sql.Timestamp;
import java.time.*;
import java.util.Date;

@FacesConverter(value = "timestampSimpleConverter")
public class TimestampSimpleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String str) {
        Timestamp timestamp = null;
        try {
            if (Utils.isNotEmpty(str)) {
                LocalDate date = Utils.parseDate(str, Utils.DATEFORMAT3);
                timestamp = Timestamp.valueOf(date.atTime(0,0,0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        String str = "";
        if (o != null && o.toString().length() >= 3  && o instanceof Timestamp) {
            str = Utils.format(((Timestamp) o).toLocalDateTime(), Utils.DATEFORMAT3);
        }
        return str;
    }
}
