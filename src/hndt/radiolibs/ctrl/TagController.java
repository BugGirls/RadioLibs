package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.TagBean;
import hndt.radiolibs.bean.TagGroupBean;
import hndt.radiolibs.biz.TagBusiness;
import hndt.radiolibs.biz.TagGroupBusiness;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.Flash;
import org.apache.commons.lang3.math.NumberUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.List;

@Named
@ViewScoped
public class TagController extends BaseController {

    TagBean bean;
    TagGroupBean gbean;
    long group_id = 0;

    public TagController() {
        group_id = NumberUtils.toLong(getParam("group_id"), 0);
        if (group_id > 0) {
            gbean = TagGroupBusiness.getInstance().load(group_id);
        }
        if (listView()) {
            pagination();
        } else if (editView()) {
            bean = TagBusiness.getInstance().load(id);
            gbean = TagGroupBusiness.getInstance().load(bean.getGroup_id());
        } else if (createView()) {
            bean = new TagBean();
            bean.setCode(TagBusiness.getInstance().maxCode() + 1);
            bean.setGroup_id(group_id);
            bean.setStatus(1);
        }
    }

    public void save() {
        int r = TagBusiness.getInstance().save(bean);
        afterAction(r);
    }

    public void delete(TagBean row) {
        bean = row;
        int r = TagBusiness.getInstance().delete(row);
        afterAction(r);
    }

    public void toggle(TagBean row) {
        bean = row;
        int r = TagBusiness.getInstance().toggleStatus(row);
        afterAction(r);
    }

    public void edit(TagBean row) {
        redirect("tag_edit.xhtml?id=" + row.getId());
    }

    @Override
    public void pagination() {
        setParamPage();
        pageBean = TagBusiness.getInstance().pagination(group_id, keyword, pageBean);
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("tag_list.xhtml?group_id=" + bean.getGroup_id());
        }
    }

    public void up(TagBean row) {
        bean = row;
        TagBean tagBean = TagBusiness.getInstance().nearByUp(row);
        if (tagBean != null) {
            TagBusiness.getInstance().updateSequence(row.getId(),tagBean.getSequence());
            TagBusiness.getInstance().updateSequence(tagBean.getId(),row.getSequence());
        }
        afterAction(1);
    }

    public void down(TagBean row) {
        bean = row;
        TagBean tagBean = TagBusiness.getInstance().nearByDown(row);
        if (tagBean != null) {
            TagBusiness.getInstance().updateSequence(row.getId(),tagBean.getSequence());
            TagBusiness.getInstance().updateSequence(tagBean.getId(),row.getSequence());
        }
        afterAction(1);
    }

    public TagBean getBean() {
        return bean;
    }

    public void setBean(TagBean bean) {
        this.bean = bean;
    }

    public TagGroupBean getGbean() {
        return gbean;
    }

    public void setGbean(TagGroupBean gbean) {
        this.gbean = gbean;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }
}
