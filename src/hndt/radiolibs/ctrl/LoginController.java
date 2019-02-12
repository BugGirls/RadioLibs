package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.LogBusiness;
import hndt.radiolibs.biz.ManagerBusiness;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.Map;

@Named
@ViewScoped
public class LoginController extends BaseController {
    ManagerBean bean = new ManagerBean();

    public LoginController() {
    }

    public void login() {
        String json = ManagerBusiness.getInstance().login(bean.getName(), bean.getPassword());
        if (Utils.isNotEmpty(json)) {
            if (json.contains("error") || json.contains("Error")) {
                Map<String, String> map = GSON.toObject(json, Map.class);
                String error = map.get("error");
                if (Utils.isNotEmpty(error)) {
                    addFlashMessage(error);
                }
            } else {
                ManagerBean mbean = GSON.toObject(json, ManagerBean.class);
                mbean = ManagerBusiness.getInstance().load(mbean.getName());
                ManagerBusiness.getInstance().saveByName(mbean);
                ManagerBusiness.getInstance().attachRoleBeanList(mbean);
                ManagerBusiness.getInstance().attachUrlList(mbean);
                putSession(EnumValue.LITERAL_MANAGER, mbean);
                redirect("index.jsp");
            }
        } else {
            addFlashMessage("网络错误");
        }
    }

    public void loginSpecial(Long user_id) {
        Logger.info(user_id + "");
        ManagerBean user = ManagerBusiness.getInstance().load(user_id);
        Logger.info(GSON.toJson(user));
        ManagerBusiness.getInstance().attachRoleBeanList(user);
        ManagerBusiness.getInstance().attachUrlList(user);
        putSession(EnumValue.LITERAL_MANAGER, user);
        LogBusiness.getInstance().save(sessionManager(), "登陆", "登陆");
        redirect("index.jsp");
    }

    public ManagerBean getBean() {
        return bean;
    }

    public void setBean(ManagerBean bean) {
        this.bean = bean;
    }

}
