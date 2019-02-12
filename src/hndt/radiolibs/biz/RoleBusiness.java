package hndt.radiolibs.biz;

import hndt.radiolibs.bean.PermissionBean;
import hndt.radiolibs.bean.RoleBean;
import hndt.radiolibs.util.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hystar
 * @date 2017/8/7
 */
public class RoleBusiness {

    public PageBean pagination(String keyword, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sql = SQL.of("SELECT id, name, permission_ids, status FROM role").and("name LIKE", keyword).append("ORDER BY id ASC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<RoleBean> permissionBeanList = DBTool.list(RoleBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(permissionBeanList);
        return pageBean;
    }

    public RoleBean load(long id) {
        RoleBean roleBean = DBTool.find(RoleBean.class, "SELECT id, name, permission_ids, status FROM role WHERE id=?", id);
        return roleBean;
    }

    public RoleBean loadIdAndStatus(long id) {
        RoleBean roleBean = DBTool.find(RoleBean.class, "SELECT id, name, permission_ids, status FROM role WHERE id=? AND status=1", id);
        return roleBean;
    }

    public int save(RoleBean roleBean) {
        int r = -1;
        if (roleBean.getId() == null || roleBean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO role(name, permission_ids, status) VALUES(?,?,?)", roleBean.getName(), roleBean.getPermission_ids(), roleBean.getStatus());
            if (id > 0) {
                roleBean.setId(id);
                r = 1;
            }
        } else {
            r = DBTool.update("UPDATE role SET name=?, permission_ids=?, status=? WHERE id=?", roleBean.getName(), roleBean.getPermission_ids(), roleBean.getStatus(), roleBean.getId());
        }
        return r;
    }

    public int toggleStatus(RoleBean roleBean) {
        int r = DBTool.update("UPDATE role SET status=? WHERE id=?", roleBean.getStatus() == 1 ? 0 : 1, roleBean.getId());
        return r;
    }

    public int clear(RoleBean roleBean) {
        int r = DBTool.update("UPDATE role SET permission_ids=? WHERE id=?", null, roleBean.getId());
        return r;
    }

    public int delete(RoleBean roleBean) {
        int r = DBTool.update("DELETE FROM role WHERE id=?", roleBean.getId());
        return r;
    }

    public List<RoleBean> list() {
        List<RoleBean> roleBeanList = DBTool.list(RoleBean.class, "SELECT * FROM role WHERE status=?", 1);
        return roleBeanList;
    }

    public List<RoleBean> listByIds(List<Long> idList) {
        return DBTool.list(RoleBean.class, "SELECT * FROM role WHERE id IN(" + SQL.toInString(idList) + ") AND status=1");
    }

    public void attachPermissionBeanList(RoleBean bean) {
        if (Utils.isNotEmpty(bean.getPermission_ids()) && !"*".equals(bean.getPermission_ids())) {
            List<Long> idList = GSON.toList(bean.getPermission_ids(), Utils.typeLong);
            List<PermissionBean> permissionBeanList = PermissionBusiness.getInstance().listByIds(idList);
            bean.setPermissionBeanList(permissionBeanList);
        }
    }

    public void attachPermissionBeanListAll(RoleBean bean) {
        List<PermissionBean> permissionBeanList = PermissionBusiness.getInstance().list();
        bean.setPermissionBeanList(permissionBeanList);
    }

    private static RoleBusiness biz;

    private RoleBusiness() {
    }

    public static synchronized RoleBusiness getInstance() {
        if (biz == null) {
            biz = new RoleBusiness();
        }
        return biz;
    }
}
