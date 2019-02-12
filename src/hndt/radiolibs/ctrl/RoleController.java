package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.PermissionBean;
import hndt.radiolibs.bean.RoleBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.PermissionBusiness;
import hndt.radiolibs.biz.RoleBusiness;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.List;

/**
 * 角色Controller
 * <p>
 * Created by Hystar on 2017/8/7.
 */
@Named
@ViewScoped
public class RoleController extends BaseController {

    private RoleBean bean;

    public RoleController() {
        if (listView()) {
            pagination();
        } else if (editView()) {
            bean = RoleBusiness.getInstance().load(id);
            if (bean != null) {
                bean.setSelectedIds(GSON.toList(bean.getPermission_ids(), Utils.typeLong));
                RoleBusiness.getInstance().attachPermissionBeanListAll(bean);
            }
        } else if (createView()) {
            bean = new RoleBean();
            bean.setStatus(EnumValue.Status.EFFECTIVE.getCode());
        }
    }

    /**
     * 获取角色信息列表
     */
    @Override
    public void pagination() {
        setParamPage();
        pageBean = RoleBusiness.getInstance().pagination(keyword, pageBean);
        List<RoleBean> roleList = pageBean.getList();
        if (Utils.isNotEmpty(roleList)) {
            for (Object obj : roleList) {
                RoleBusiness.getInstance().attachPermissionBeanList((RoleBean) obj);
            }
        }
    }

    /**
     * 添加或更新角色信息
     */
    public void save() {
        String ids = null;
        if (Utils.isNotEmpty(bean.getSelectedIds())) {
            ids = (GSON.toJson(bean.getSelectedIds()));
        }
        bean.setPermission_ids(ids);

        String errorMessage = validate(bean);
        if (Utils.isNotEmpty(errorMessage)) {
            addTip(errorMessage);
        } else {
            int r = RoleBusiness.getInstance().save(bean);
            afterAction(r);
        }
    }
    private String validate(RoleBean bean) {
        String errorMessage = null;
        if (Utils.isEmpty(bean.getName())) {
            errorMessage = "请输入角色名称";
        }
        return errorMessage;
    }

    /**
     * 带参数的跳转
     *
     * @param row
     */
    public void edit(RoleBean row) {
        redirect("role_edit.xhtml?id=" + row.getId());
    }

    /**
     * 启用与禁用
     *
     * @param row
     */
    public void toggle(RoleBean row) {
        int r = RoleBusiness.getInstance().toggleStatus(row);
        afterAction(r);
    }

    /**
     * 清空角色权限
     *
     * @param row
     */
    public void clear(RoleBean row) {
        int r = RoleBusiness.getInstance().clear(row);
        afterAction(r);
    }

    /**
     * 删除角色
     *
     * @param row
     */
    public void delete(RoleBean row) {
        int r = RoleBusiness.getInstance().delete(row);
        afterAction(r);
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("role_list.xhtml");
        }
    }

    public RoleBean getBean() {
        return bean;
    }

    public void setBean(RoleBean bean) {
        this.bean = bean;
    }
}
