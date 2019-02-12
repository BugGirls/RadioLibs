package hndt.radiolibs.ctrl;

import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ResBusiness;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class ResPublishController extends ResController {


    public void save() {
        int r = ResBusiness.getInstance().savePublisher(bean);
        addTip(r);
        tab(bean, EnumValue.Section.PUBLISH);
    }

}


