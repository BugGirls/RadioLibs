package hndt.radiolibs.jsf;

import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

@FacesConverter(value = "doubleConverter")
public class DoubleConverter implements Converter {

    private static final DecimalFormat format = new DecimalFormat("#.#");

    @Override
    public Object getAsObject(FacesContext fc, UIComponent uic, String str) {
        Double d = 0d;
        try {
            if (Utils.isNotEmpty(str)) {
                str = full2HalfChange(str);
                Number number = format.parse(str);
                d = number.doubleValue();
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return d;
    }

    @Override
    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
        String str = null;
        if (o != null && o instanceof Double) {
            Double d = (Double) o;
            if (Utils.isNotEmpty(d)) {
                str = format.format(d);
            }
        }
        return str;
    }

    private static final String full2HalfChange(String QJstr) throws UnsupportedEncodingException {
        StringBuilder outStrBuf = new StringBuilder("");
        String Tstr = "";
        byte[] b = null;
        for (int i = 0; i < QJstr.length(); i++) {
            Tstr = QJstr.substring(i, i + 1);
            if (Tstr.equals("　")) {
                outStrBuf.append(" ");
                continue;
            }
            b = Tstr.getBytes("unicode");
            if (b[2] == -1) {
                b[3] = (byte) (b[3] + 32);
                b[2] = 0;
                outStrBuf.append(new String(b, "unicode"));
            } else {
                outStrBuf.append(Tstr);
            }
        } // end for.
        return outStrBuf.toString();
    }
}
