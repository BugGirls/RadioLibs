package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.biz.AppBusiness;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.util.GSON;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.List;

@Named
@ViewScoped
public class ResTitleController extends ResController {


    public void save() {
        int r = ResBusiness.getInstance().saveTitle(bean);
        addTip(r);
        tab(bean, EnumValue.Section.TITLE);
    }

}


