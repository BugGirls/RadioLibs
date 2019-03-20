package hndt.radiolibs.biz;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.bean.RoleBean;
import hndt.radiolibs.bean.TagBean;
import hndt.radiolibs.bean.TagGroupBean;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.PageBean;
import hndt.radiolibs.util.SQL;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TagGroupBusiness {

    public int save(TagGroupBean bean) {
        int r = 0;
        if (bean.getId() == 0) {
            long id = DBTool.insert("INSERT INTO tag_group(manager_id,sequence,repeatable,naturally,name,system,status) VALUES (?,?,?,?,?,?,?)", bean.getManager_id(), bean.getSequence(), bean.getRepeatable(), bean.getNaturally(), bean.getName(), bean.getSystem(), bean.getStatus());
            bean.setId(id);
            bean.setSequence(id);
            save(bean);
            r = 1;
        } else {
            r = DBTool.update("UPDATE tag_group SET sequence=?,repeatable=?,naturally=?,name=?,system=?,status=? WHERE id=?", bean.getSequence(), bean.getRepeatable(), bean.getNaturally(), bean.getName(), bean.getSystem(),bean.getStatus(), bean.getId());
        }
        return r;
    }

    public int delete(TagGroupBean bean) {
        int r = DBTool.update("DELETE FROM tag_group WHERE id=?", bean.getId());
        return r;
    }

    public TagGroupBean load(long id) {
        TagGroupBean bean = DBTool.find(TagGroupBean.class, "SELECT * FROM tag_group WHERE id=?", id);
        return bean;
    }

    public TagGroupBean load(String name) {
        TagGroupBean bean = DBTool.find(TagGroupBean.class, "SELECT * FROM tag_group WHERE name=?", name);
        return bean;
    }

    public List<TagGroupBean> listForManager(Long manager_id) {
        String managers = ManagerGroupBusiness.getInstance().group(manager_id);
        SQL sql = SQL.of("SELECT * FROM tag_group").and("manager_id in", managers).append(" AND status=1 ").append("ORDER BY sequence ASC");
        Logger.info("---------------------////" + sql.toString());
        List<TagGroupBean> list = DBTool.list(TagGroupBean.class, sql.sql(), sql.params());
        return list;
    }

    public List<TagGroupBean> list(Long manager_id) {
        String managers = ManagerGroupBusiness.getInstance().group(manager_id);
        SQL sql = SQL.of("SELECT * FROM tag_group").or("manager_id in", managers, "system=", 1).append(" AND status=1 ").append("ORDER BY sequence ASC");
        List<TagGroupBean> list = DBTool.list(TagGroupBean.class, sql.sql(), sql.params());
        return list;
    }

    public void attachChildren(List<TagGroupBean> list) {
        for (TagGroupBean groupBean : list) {
            groupBean.setChildren(TagBusiness.getInstance().list(groupBean.getId()));
        }
    }

    public PageBean pagination(Long manager_id, String keyword, boolean includeSystem, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sql = SQL.of("SELECT * FROM tag_group");
        sql = includeSystem ? sql.or("manager_id=", manager_id, "system=", 1) : sql.or("manager_id=", manager_id);
        sql.and("name LIKE", keyword).append("ORDER BY sequence ASC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<TagGroupBean> list = DBTool.list(TagGroupBean.class, sql.sql(), sql.params());
        pageBean.setRows(DBTool.field(Long.class, sql.countSql(), sql.countParams()));
        pageBean.setList(list);
        return pageBean;
    }

    public int toggleStatus(TagGroupBean bean) {
        int r = DBTool.update("UPDATE tag_group SET status=? WHERE id=?", bean.getStatus() == 1 ? 0 : 1, bean.getId());
        return r;
    }

    public boolean containsTag(TagGroupBean group, Integer code) {
        Optional<TagBean> has = group.getChildren().stream().filter(x -> Objects.equals(code, x.getCode())).findAny();
        return has.isPresent();
    }

    private static TagGroupBusiness biz = null;

    private TagGroupBusiness() {
    }

    public synchronized static TagGroupBusiness getInstance() {
        if (biz == null) {
            biz = new TagGroupBusiness();
        }
        return biz;
    }
}
