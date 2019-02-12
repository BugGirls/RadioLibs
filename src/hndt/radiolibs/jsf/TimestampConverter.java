package hndt.radiolibs.jsf;

import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.math.NumberUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Date;

@FacesConverter(value = "timestampConverter")
public class TimestampConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String str) {
        Timestamp timestamp = null;
        try {
            if (Utils.isNotEmpty(str)) {
                LocalDateTime date = Utils.parse(str, Utils.DATEFORMAT2);
                timestamp = Timestamp.valueOf(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        String str = "";
        if (o != null && o.toString().length() >= 3 && o instanceof Timestamp) {
            str = Utils.format(((Timestamp) o).toLocalDateTime(), Utils.DATEFORMAT2);
        }
        return str;
    }
}
