package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.PermissionBean;
import hndt.radiolibs.biz.PermissionBusiness;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.Utils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * 权限Controller
 * <p>
 * @author Hystar
 * @date 2017/8/4
 */
@Named
@ViewScoped
public class PermissionController extends BaseController {
    PermissionBean permissionBean;

    public PermissionController() {
        if (listView()) {
            pagination();
        } else if (editView()) {
            permissionBean = PermissionBusiness.getInstance().load(id);
        } else if (createView()) {
            permissionBean = new PermissionBean();
            permissionBean.setStatus(1);
        }
    }

    /**
     * 获取权限信息列表
     */
    @Override
    public void pagination() {
        setParamPage();
        pageBean = PermissionBusiness.getInstance().pagination(keyword, pageBean);
    }

    /**
     * 带参数的跳转
     *
     * @param permissionBean
     */
    public void edit(PermissionBean permissionBean) {
        redirect("permission_edit.xhtml?id=" + permissionBean.getId());
    }

    /**
     * 保存或更新
     */
    public void save() {
        String errorMessage = validate(permissionBean);
        if (Utils.isNotEmpty(errorMessage)) {
            addTip(errorMessage);
        } else {
            int r = PermissionBusiness.getInstance().save(permissionBean);
            afterAction(r);
        }
    }

    private String validate(PermissionBean permissionBean) {
        String errorMessage = null;
        if (Utils.isEmpty(permissionBean.getName())) {
            errorMessage = "请输入权限名称";
        } else if (Utils.isEmpty(permissionBean.getUrls())) {
            errorMessage = "请填写权限地址";
        }
        return errorMessage;
    }

    /**
     * 启用与禁用
     *
     * @param permissionBean
     */
    public void toggle(PermissionBean permissionBean) {
        int r = PermissionBusiness.getInstance().toggleStatus(permissionBean);
        afterAction(r);
    }

    /**
     * 删除权限
     *
     * @param permissionBean
     */
    public void delete(PermissionBean permissionBean) {
        int r = PermissionBusiness.getInstance().delete(permissionBean);
        afterAction(r);
    }

    /**
     * 分页操作
     *
     * @param page
     */
    @Override
    public void toPage(int page) {
        pageBean.setPage(page);
        pagination();
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("permission_list.xhtml");
        }
    }

    public PermissionBean getPermissionBean() {
        return permissionBean;
    }

    public void setPermissionBean(PermissionBean permissionBean) {
        this.permissionBean = permissionBean;
    }
}
