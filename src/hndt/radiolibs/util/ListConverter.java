package hndt.radiolibs.util;

import javax.faces.convert.FacesConverter;

@FacesConverter(value = "listConverter")
public class ListConverter  {

//    @Override
//    public List<String> getAsObject(FacesContext fc, UIComponent uic, String str) {
//        List<String> list = null;
//        try {
//            if (util.isNotEmpty(str)) {
//                list = util.asList(str);
//            }
//        } catch (Exception e) {
//            Logger.error(e);
//        }
//        return list;
//    }
//
//    @Override
//    public String getAsString(FacesContext fc, UIComponent uic, Object o) {
//        String str = null;
//        if (o != null && o instanceof List) {
//            str = util.asString((List)o);
//        }
//        return str;
//    }

}
