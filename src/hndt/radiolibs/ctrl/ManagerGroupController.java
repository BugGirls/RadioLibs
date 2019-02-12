package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.bean.ManagerGroupBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ManagerGroupBusiness;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 用户Controller
 *
 * @author Hystar
 * @date 2017/8/4
 */
@Named
@ViewScoped
public class ManagerGroupController extends BaseController {

    ManagerGroupBean bean;

    public ManagerGroupController() {
        if (listView()) {
            pagination();
        } else if (editView()) {
            bean = ManagerGroupBusiness.getInstance().load(id);
        } else if (createView()) {
            bean = new ManagerGroupBean();
        }
    }

    @Override
    public void pagination() {
        setParamPage();
        pageBean = ManagerGroupBusiness.getInstance().pagination(keyword, pageBean);
    }


    /**
     * 保存权限
     */
    public void save() {
        int r = ManagerGroupBusiness.getInstance().save(bean);
        afterAction(r);
    }

    /**
     * 删除记录
     *
     * @param row
     */
    public void delete(ManagerGroupBean row) {
        int r = ManagerGroupBusiness.getInstance().delete(row);
        afterAction(r);
    }

    /**
     * 清除角色
     *
     * @param bean
     */
    public void clear(ManagerGroupBean bean) {
        int r = ManagerGroupBusiness.getInstance().save(bean);
        afterAction(r);
    }


    /**
     * 跳转到编辑页面
     *
     * @param row
     */
    public void edit(ManagerBean row) {
        redirect("manager_group_edit.xhtml?id=" + row.getId());
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("manager_group_list.xhtml");
        }
    }

    public ManagerGroupBean getBean() {
        return bean;
    }

    public void setBean(ManagerGroupBean bean) {
        this.bean = bean;
    }
}
