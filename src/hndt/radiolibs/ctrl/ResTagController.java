package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.TagBean;
import hndt.radiolibs.bean.TagGroupBean;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Empress
 */
@Named
@ViewScoped
public class ResTagController extends ResController {

    List<TagGroupBean> groups = new ArrayList<>();

    public ResTagController() {
        groups = TagGroupBusiness.getInstance().list(getManagerIdParam());
        TagGroupBusiness.getInstance().attachChildren(groups);
        if (bean.getId() != null) {
            ResBusiness.getInstance().attachSelectedTags(sessionManager(), bean, groups);
        }
    }

    public void selectTag(TagGroupBean group, TagBean tag) {
        if (bean.getTagList() == null) {
            bean.setTagList(new ArrayList<>(5));
        }
        if (bean.getTagList().contains(tag.getCode())) {
            bean.getTagList().remove(tag.getCode());
        } else {
            if (group.getRepeatable() == 0) {
                for (TagBean t : group.getChildren()) {
                    bean.getTagList().remove(t.getCode());
                }
            }
            bean.getTagList().add(tag.getCode());
        }
    }

    /**
     * 标签状态是否是选中
     *
     * @param group
     * @param tag
     * @return
     */
    public boolean isActive(TagGroupBean group, TagBean tag) {
        if (bean.getTagList() == null) {
            bean.setTagList(new ArrayList<>(5));
        }
        return bean.getTagList().contains(tag.getCode());
    }

    /**
     * 标签是否可选. 系统库只能选主观标签，自有库完全可选
     *
     * @param group
     * @return
     */
    public boolean selectable(TagGroupBean group) {
        long manager_id = getManagerIdParam();
        String inGroup = ManagerGroupBusiness.getInstance().group(manager_id);
        List<String> inGroupList = Utils.asList(inGroup);
        if (bean.getManager_id() == manager_id || inGroupList.contains(String.valueOf(manager_id))) {
            return true;
        } else if (bean.getSystem() == 1 && group.getNaturally() == 0) {
            return true;
        }
        return false;
    }

    public void saveForLib() {
        bean.setType_tags(Utils.asString(bean.getTagList()));
        int r = ResBusiness.getInstance().saveTypeTags(bean);
        addTip(r);
        tab(bean, EnumValue.Section.TAG);
    }

    /**
     * 保存资源类别信息
     * 如果是频率管理员上传的资源文件，当频率管理员保存资源类别时，可以保存任何类别，并将类别信息都保存到respository表中
     * 如果是库管理员上传的资源文件，当频率管理员保存资源类别时，只能保存主观属性（naturally=1），并将主观属性的类别信息保存到res_tag_custom表中
     */
    public void saveForChannelManager() {
        bean.setType_tags(Utils.asString(bean.getTagList()));
        int r = -1;
        if (bean.getSystem() == 0) {//频率管理员上传的资源文件
            r = ResBusiness.getInstance().saveTypeTags(bean);
        } else { //系统库管理员上传的资源文件，保存到res_tag_custom表中
            r = ResBusiness.getInstance().saveTypeTagsForChannelManager(getManagerIdParam(), bean);
        }
        addTip(r);
        tab(bean, EnumValue.Section.TAG);
    }

    public List<TagGroupBean> getGroups() {
        return groups;
    }

    public void setGroups(List<TagGroupBean> groups) {
        this.groups = groups;
    }
}


