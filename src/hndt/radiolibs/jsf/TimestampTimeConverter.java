package hndt.radiolibs.jsf;

import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Hystar on 2017/7/21.
 */
@FacesConverter(value = "timestampTimeConverter")
public class TimestampTimeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String str) {
        Timestamp timestamp = null;

        if (Utils.isNotEmpty(str)) {
            String[] times = str.split(":");
            int hour = NumberUtils.toInt(times[0], 0);
            int minute = NumberUtils.toInt(times[1], 0);

            LocalDateTime localDateTime = LocalDate.now().atTime(hour, minute);
            timestamp = Timestamp.valueOf(localDateTime);
        }

        return timestamp;
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object o) {
        String str = "";
        if (o != null && o.toString().length() >= 3  && o instanceof Timestamp) {
            str = Utils.format(((Timestamp) o).toLocalDateTime(), Utils.DATEFORMAT7);
        }
        return str;
    }

}
