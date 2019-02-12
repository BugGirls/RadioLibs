package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.TagGroupBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.TagGroupBusiness;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.PageBean;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.Objects;

@Named
@ViewScoped
public class TagGroupController extends BaseController {

    TagGroupBean bean;

    public TagGroupController() {
        if (listView()) {
            pagination();
        } else if (editView()) {
            bean = TagGroupBusiness.getInstance().load(id);
        } else if (createView()) {
            bean = new TagGroupBean();
            bean.setManager_id(manager_id);
            bean.setNaturally(EnumValue.Naturally.SUBJECTIVITY.getCode());// 主观属性
            bean.setStatus(EnumValue.Status.EFFECTIVE.getCode());// 有效
        }
    }

    public void setSystem() {
        bean.setSystem(1);
    }

    public void save() {
        int r = TagGroupBusiness.getInstance().save(bean);
        afterAction(r);
    }

    public void delete(TagGroupBean row) {
        int r = TagGroupBusiness.getInstance().delete(row);
        afterAction(r);
    }

    public void copy(TagGroupBean bean) {
        TagGroupBean aBean = new TagGroupBean();
        aBean.setName(bean.getName() + "复制");
        aBean.setManager_id(bean.getManager_id());
        aBean.setRepeatable(bean.getRepeatable());
        aBean.setSequence(bean.getSequence());
        aBean.setNaturally(bean.getNaturally());
        int r = TagGroupBusiness.getInstance().save(aBean);
        afterAction(r);
    }

    public void clear(TagGroupBean bean) {
        int r = TagGroupBusiness.getInstance().save(bean);
        afterAction(r);
    }

    public void toggle(TagGroupBean bean) {
        int r = TagGroupBusiness.getInstance().toggleStatus(bean);
        afterAction(r);
    }

    public void edit(TagGroupBean row) {
        redirect("tag_group_edit.xhtml?id=" + row.getId());
    }

    public void tags(TagGroupBean row) {
        pageBean = new PageBean();
        redirect("tag_list.xhtml?group_id=" + row.getId());
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("tag_group_list.xhtml");
        }
    }

    @Override
    public void pagination() {
        setParamPage();
        final boolean[] includeSystem = {false};
        sessionManager().getRoleList().stream().filter(x -> Objects.equals(101L, x.getId())).findAny().ifPresent(x -> includeSystem[0] = true);
        pageBean = TagGroupBusiness.getInstance().pagination(manager_id, keyword, includeSystem[0], pageBean);
    }

    public TagGroupBean getBean() {
        return bean;
    }

    public void setBean(TagGroupBean bean) {
        this.bean = bean;
    }

}
