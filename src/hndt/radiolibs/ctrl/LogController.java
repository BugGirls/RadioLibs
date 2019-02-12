package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.LogBean;
import hndt.radiolibs.bean.TagGroupBean;
import hndt.radiolibs.biz.LogBusiness;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.PageBean;

import javax.faces.annotation.RequestMap;
import javax.faces.annotation.RequestParameterMap;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.Map;

@Named
@ViewScoped
public class LogController extends BaseController {

    public LogController() {
        if (listView()) {
            pagination();
        }
    }

    public void delete(LogBean row) {
        int r = LogBusiness.getInstance().delete(row);
        afterAction(r);
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("log_list.xhtml");
        }
    }

    @Override
    public void pagination() {
        pageBean = LogBusiness.getInstance().pagination(manager_id, keyword, pageBean);
    }

}
