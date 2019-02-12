package hndt.radiolibs.biz;

import hndt.radiolibs.bean.TagBean;
import hndt.radiolibs.bean.TagGroupBean;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.PageBean;
import hndt.radiolibs.util.SQL;
import hndt.radiolibs.util.Utils;

import java.util.Collections;
import java.util.List;

public class TagBusiness {

    public int save(TagBean bean) {
        int r = 0;
        if (bean.getId() == 0) {
            bean.setCode(maxCode() + 1);
            long id = DBTool.insert("INSERT INTO tag(group_id,sequence,wrap,code,name,status) VALUES (?,?,?,?,?,?)", bean.getGroup_id(), bean.getSequence(), bean.getWrap(), bean.getCode(), bean.getName(), bean.getStatus());
            if (id > 0) {
                bean.setId(id);
                DBTool.update("UPDATE tag SET sequence=? WHERE id=?", id, id);
                r = 1;
            }
        } else {
            r = DBTool.update("UPDATE tag SET group_id=?,sequence=?,wrap=?,code=?,name=?,status=? WHERE id=?", bean.getGroup_id(), bean.getSequence(), bean.getWrap(), bean.getCode(), bean.getName(), bean.getStatus(), bean.getId());
        }
        return r;
    }

    public int delete(TagBean bean) {
        int r = DBTool.update("DELETE FROM tag WHERE id=?", bean.getId());
        return r;
    }

    public List<TagBean> list(long group_id) {
        List<TagBean> list = DBTool.list(TagBean.class, "SELECT * FROM tag WHERE group_id=? AND status=1 ORDER BY sequence ASC", group_id);
        return list;
    }

    public TagGroupBean loadGroup(Integer tag) {
        TagGroupBean bean = DBTool.find(TagGroupBean.class, "SELECT g.* FROM tag_group g,tag WHERE tag.group_id=g.id AND tag.code=?", tag);
        return bean;
    }

    public TagBean load(long id) {
        TagBean bean = DBTool.find(TagBean.class, "SELECT * FROM tag WHERE id=?", id);
        if (bean != null) {
        }
        return bean;
    }

    public TagBean load(int code) {
        TagBean bean = DBTool.find(TagBean.class, "SELECT * FROM tag WHERE code=?", code);
        return bean;
    }

    public TagBean validateLoad(int code) {
        return DBTool.find(TagBean.class, "SELECT t.* FROM tag t,tag_group g WHERE t.group_id=g.id AND g.status=1 AND t.code='"+String.valueOf(code)+"' AND t.status=1");
    }

    public int maxCode() {
        Integer field = DBTool.field(Integer.class, "SELECT MAX(code) FROM tag");
        return field;
    }

    public TagBean nearByDown(TagBean row) {
        TagBean bean = DBTool.find(TagBean.class, "SELECT * FROM tag WHERE group_id=? AND sequence < ? ORDER BY sequence DESC LIMIT 0,1", row.getGroup_id(), row.getSequence());
        return bean;
    }

    public TagBean nearByUp(TagBean row) {
        TagBean bean = DBTool.find(TagBean.class, "SELECT * FROM tag WHERE group_id=? AND sequence > ? ORDER BY sequence ASC LIMIT 0,1", row.getGroup_id(), row.getSequence());
        return bean;
    }

    public PageBean pagination(long group_id, String keyword, PageBean pageBean) {
        pageBean = pageBean == null ? new PageBean() : pageBean;
        SQL sqlb = SQL.of("SELECT * FROM tag").and("group_id=", group_id).or("name LIKE", keyword, "code LIKE", keyword).append("ORDER BY sequence ASC").limit(pageBean.getStart(), pageBean.getLinesPerPage());
        List<TagBean> list = DBTool.list(TagBean.class, sqlb.sql(), sqlb.params());
        pageBean.setRows(DBTool.field(Long.class, sqlb.countSql(), sqlb.countParams()));
        pageBean.setList(list);
        return pageBean;
    }

    public int toggleStatus(TagBean bean) {
        int r = DBTool.update("UPDATE tag SET status=? WHERE id=?", bean.getStatus() == 1 ? 0 : 1, bean.getId());
        return r;
    }

    public int updateSequence(long id, long sequence) {
        int r = DBTool.update("UPDATE tag SET sequence=? WHERE id=?", sequence, id);
        return r;
    }

    public List<TagBean> listByCodes(List<String> codes) {
        if (codes==null || codes.size()==0){
            return Collections.EMPTY_LIST;
        }
        return DBTool.list(TagBean.class, "SELECT t.* FROM tag t,tag_group g WHERE t.group_id=g.id AND g.status=1 AND t.code IN("+SQL.toInString(codes)+") AND t.status=1");
    }

    public TagBean loadByName(String name) {
        return DBTool.find(TagBean.class, "SELECT * FROM tag WHERE name=? AND status=1", name);
    }

    private static TagBusiness biz = null;

    private TagBusiness() {
    }

    public synchronized static TagBusiness getInstance() {
        if (biz == null) {
            biz = new TagBusiness();
        }
        return biz;
    }


}
