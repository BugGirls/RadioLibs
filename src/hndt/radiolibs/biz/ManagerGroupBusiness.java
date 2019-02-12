package hndt.radiolibs.biz;

import hndt.radiolibs.bean.PermissionBean;
import hndt.radiolibs.bean.ManagerGroupBean;
import hndt.radiolibs.util.*;

import java.util.List;

/**
 * @date 2017/8/7
 */
public class ManagerGroupBusiness {

    public PageBean pagination(String keyword, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sql = SQL.of("SELECT * FROM manager_group").and("name LIKE", keyword).append("ORDER BY id ASC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<ManagerGroupBean> permissionBeanList = DBTool.list(ManagerGroupBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(permissionBeanList);
        return pageBean;
    }

    public ManagerGroupBean load(long id) {
        ManagerGroupBean ManagerGroupBean = DBTool.find(ManagerGroupBean.class, "SELECT * FROM manager_group WHERE id=?", id);
        return ManagerGroupBean;
    }

    public int save(ManagerGroupBean bean) {
        int r = -1;
        if (bean.getId() == null || bean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO manager_group(name, manager_ids) VALUES(?,?)", bean.getName(), bean.getManager_ids());
            if (id > 0) {
                bean.setId(id);
                r = 1;
            }
        } else {
            r = DBTool.update("UPDATE manager_group SET name=?, manager_ids=? WHERE id=?", bean.getName(), bean.getManager_ids(), bean.getId());
        }
        return r;
    }


    public int delete(ManagerGroupBean bean) {
        int r = DBTool.update("DELETE FROM manager_group WHERE id=?", bean.getId());
        return r;
    }

    public List<ManagerGroupBean> list() {
        List<ManagerGroupBean> list = DBTool.list(ManagerGroupBean.class, "SELECT * FROM manager_group WHERE status=?", 1);
        return list;
    }

    public ManagerGroupBean find(long manager_id) {
        ManagerGroupBean bean = DBTool.find(ManagerGroupBean.class, "SELECT * FROM manager_group WHERE FIND_IN_SET(" + manager_id + ",manager_ids) ");
        return bean;
    }

    public String group(long manager_id) {
        ManagerGroupBean bean = find(manager_id);
        return bean == null ? String.valueOf(manager_id) : bean.getManager_ids();
    }

    private static ManagerGroupBusiness biz;

    private ManagerGroupBusiness() {
    }

    public static synchronized ManagerGroupBusiness getInstance() {
        if (biz == null) {
            biz = new ManagerGroupBusiness();
        }
        return biz;
    }
}
