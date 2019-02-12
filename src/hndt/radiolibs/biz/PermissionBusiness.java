package hndt.radiolibs.biz;

import hndt.radiolibs.bean.PermissionBean;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.PageBean;
import hndt.radiolibs.util.SQL;
import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 权限Business
 * <p>
 * Created by Hystar on 2017/8/4.
 */
public class PermissionBusiness {

    public PageBean pagination(String keyword, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;

        SQL sql = SQL.of("SELECT id, name, urls, status FROM permission").and("name LIKE", keyword).append("ORDER BY id DESC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<PermissionBean> permissionBeanList = DBTool.list(PermissionBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(permissionBeanList);

        return pageBean;
    }

    public PermissionBean load(long id) {
        PermissionBean permissionBean = DBTool.find(PermissionBean.class, "SELECT id, name, urls, status FROM permission WHERE id=?", id);
        return permissionBean;
    }

    public int save(PermissionBean permissionBean) {
        int r = -1;
        if (permissionBean.getId() == null || permissionBean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO permission(name, urls, status) VALUES(?,?,?)", permissionBean.getName(), permissionBean.getUrls(), permissionBean.getStatus());
            if (id > 0) {
                r = 1;
            }
        } else {
            r = DBTool.update("update permission set name=?, urls=?, status=? where id=?", permissionBean.getName(), permissionBean.getUrls(), permissionBean.getStatus(), permissionBean.getId());
        }

        return r;
    }

    public int toggleStatus(PermissionBean permissionBean) {
        int r = DBTool.update("UPDATE permission SET status=? WHERE id=?", permissionBean.getStatus() == 1 ? 0 : 1, permissionBean.getId());
        return r;
    }

    public int delete(PermissionBean permissionBean) {
        int r = DBTool.update("DELETE FROM permission WHERE id=?", permissionBean.getId());
        return r;
    }

    public List<PermissionBean> list() {
        List<PermissionBean> permissionBeanList = DBTool.list(PermissionBean.class, "SELECT * FROM permission WHERE status=1");
        return permissionBeanList;
    }

    public List<PermissionBean> listByIds(List<Long> idList) {
        List<PermissionBean> list = Collections.EMPTY_LIST;
        if (Utils.isNotEmpty(idList)) {
            list = DBTool.list(PermissionBean.class, "SELECT * FROM permission WHERE id IN(" + SQL.toInString(idList) + ") AND status=1");
        }
        return list;
    }

    public void attachUrlList(PermissionBean bean) {
        List<String> list = Utils.split(bean.getUrls());
        bean.setUrlList(list);
    }

    private static PermissionBusiness biz;

    private PermissionBusiness() {
    }

    public static synchronized PermissionBusiness getInstance() {
        if (biz == null) {
            biz = new PermissionBusiness();
        }

        return biz;
    }
}
