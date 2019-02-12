package hndt.radiolibs.ctrl;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

@Named
@ViewScoped
public class RadioController extends BaseController {
    public RadioController(){
    }

    public void test(){
        addTip(-1,"测试下");
    }
}
