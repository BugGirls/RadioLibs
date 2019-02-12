package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ColumnMarkBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.util.GSON;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ResFormatController extends ResController {

    public ResFormatController() {
        ResBusiness.getInstance().attachMarkBean(bean);
        if (bean.getMarkBean() == null) {
            bean.setMarkBean(new ColumnMarkBean());
        }
    }

    public void save() {
        if (bean.getMarkBean() != null) {
            bean.setFormat_mark(GSON.toJson(bean.getMarkBean()));
        }
        int r = ResBusiness.getInstance().saveFormat(bean);
        addTip(r);
        tab(bean, EnumValue.Section.FORMAT);
    }

}


