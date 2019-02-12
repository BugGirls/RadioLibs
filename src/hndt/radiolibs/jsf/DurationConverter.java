package hndt.radiolibs.jsf;

import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@FacesConverter(value = "durationConverter")
public class DurationConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String str) {
        Long d = null;
        if (Utils.isNotEmpty(str)) {
            int idx = str.indexOf(':');
            if (idx < 0) {
                idx = str.indexOf('：');
            }
            if (idx > 0) {
                int minute = NumberUtils.toInt(str.substring(0, idx), -1);
                int second = NumberUtils.toInt(str.substring(idx + 1), -1);
                d = Duration.ofMinutes(minute).plusSeconds(second).getSeconds();
            }
        }
        return d;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        String str = "";
        if (o != null && o instanceof Long) {
            LocalTime time = LocalTime.ofSecondOfDay((Long) o);
            str = String.format("%s:%s", time.getMinute(), time.getSecond());
        }
        return str;
    }
}
