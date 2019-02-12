package hndt.radiolibs.biz;

import hndt.radiolibs.bean.LogBean;
import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.PageBean;
import hndt.radiolibs.util.SQL;

import java.util.List;

public class LogBusiness {

    public int save(ManagerBean manager, String action, String title) {
        long id = DBTool.insert("INSERT INTO log(manager_id,manager_name,title,action) VALUES (?,?,?,?)", manager.getId(), manager.getName(), title, action);
        if (id > 0) {
            return 1;
        }
        return 0;
    }

    public int delete(LogBean bean) {
        int r = DBTool.update("DELETE FROM log WHERE id=?", bean.getId());
        return r;
    }

    public PageBean pagination(Long manager_id, String keyword, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sqlb = SQL.of("SELECT * FROM log").and("manager_id=", manager_id).or("title LIKE", keyword, "action LIKE", keyword).append("ORDER BY id ASC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<LogBean> list = DBTool.list(LogBean.class, sqlb.sql(), sqlb.params());
        pageBean.setRows(DBTool.field(Long.class, sqlb.countSql(), sqlb.countParams()));
        pageBean.setList(list);
        return pageBean;
    }

    private static LogBusiness biz = null;

    private LogBusiness() {
    }

    public synchronized static LogBusiness getInstance() {
        if (biz == null) {
            biz = new LogBusiness();
        }
        return biz;
    }
}
