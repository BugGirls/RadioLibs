package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.bean.RoleBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ManagerBusiness;
import hndt.radiolibs.biz.RoleBusiness;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 用户Controller
 *
 * @author Hystar
 * @date 2017/8/4
 */
@Named
@ViewScoped
public class ManagerController extends BaseController {

    ManagerBean bean;

    public ManagerController() {
        if (listView()) {
            pagination();
        } else if (editView()) {
            bean = ManagerBusiness.getInstance().load(id);
            if (bean != null) {
                bean.setSelectedId(GSON.toList(bean.getRole_ids(), Utils.typeLong));
                ManagerBusiness.getInstance().attachRoleBeanListAll(bean);
            }
        } else if (createView()) {
            bean = new ManagerBean();
            bean.setStatus(EnumValue.Status.EFFECTIVE.getCode());
        }
    }

    @Override
    public void pagination() {
        setParamPage();
        pageBean = ManagerBusiness.getInstance().pagination(keyword, pageBean);
        List<ManagerBean> managerBeanList = pageBean.getList();
        if (Utils.isNotEmpty(managerBeanList)) {
            managerBeanList.forEach(managerBean -> ManagerBusiness.getInstance().attachRoleBeanList(managerBean));
        }
    }

    public List<EnumValue.Role> fromEnum() {
        List<EnumValue.Role> roles = new ArrayList<>(Arrays.asList(EnumValue.Role.values()));
        int role_id = 1;
        // ordinal()方法：返回此枚举常量的序数（其枚举声明中的位置，其中初始常量分配的序数为零）
        int ordinal = EnumValue.Role.instances(role_id).ordinal();
        roles.removeIf(x -> x.ordinal() <= ordinal);
        return roles;
    }

    /**
     * 保存权限
     */
    public void save() {
        if (bean.getSelectedId().size() != 0 && bean.getSelectedId() != null) {
            bean.setRole_ids(GSON.toJson(bean.getSelectedId()));
        } else {
            bean.setRole_ids(null);
        }
        int r = ManagerBusiness.getInstance().save(bean);
        afterAction(r);
    }

    public List<ManagerBean> listByStatus() {
        List<ManagerBean> managerBeanList = ManagerBusiness.getInstance().list();
        return managerBeanList;
    }

    /**
     * 删除记录
     * @param row
     */
    public void delete(ManagerBean row) {
        int r = ManagerBusiness.getInstance().delete(row);
        afterAction(r);
    }

    /**
     * 清除角色
     * @param bean
     */
    public void clear(ManagerBean bean) {
        bean.setRole_ids(null);
        int r = ManagerBusiness.getInstance().save(bean);
        afterAction(r);
    }

    /**
     * 是否禁用
     * @param bean
     */
    public void toggle(ManagerBean bean) {
        int r = ManagerBusiness.getInstance().toggleStatus(bean);
        afterAction(r);
    }

    /**
     * 跳转到编辑页面
     * @param row
     */
    public void edit(ManagerBean row) {
        redirect("manager_edit.xhtml?id=" + row.getId());
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("manager_list.xhtml");
        }
    }

    public void managerIdsToName(String managerIds) {
        List<Long> idList = Utils.asListLong(managerIds);
        List<ManagerBean> managerBeanList = ManagerBusiness.getInstance().listByIds(idList);
        List<String> nameList = new ArrayList<>();
        managerBeanList.forEach(managerBean -> nameList.add(managerBean.getName()));
    }

    public ManagerBean getBean() {
        return bean;
    }

    public void setBean(ManagerBean bean) {
        this.bean = bean;
    }

}
