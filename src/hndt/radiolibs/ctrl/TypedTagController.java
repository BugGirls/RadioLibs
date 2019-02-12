package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.TagBean;
import hndt.radiolibs.bean.TagGroupBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.TagGroupBusiness;
import hndt.radiolibs.biz.TypedBusiness;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 规则标签Controller
 * <p>
 * Created by girl on 2017/7/22.
 */
@Named
@ViewScoped
public class TypedTagController extends TypedController {

    List<TagGroupBean> groups = new ArrayList<>();

    public TypedTagController() {
        groups = TagGroupBusiness.getInstance().list(getManagerIdParam());// 将标签组存入List集合中
        TagGroupBusiness.getInstance().attachChildren(groups);// 将标签放入所属的标签组中
        TypedBusiness.getInstance().attachTagList(bean);
    }

    /**
     * 填充标签组中的标签（从数据库中获取该规则所属标签并在页面中显示为选中状态）
     *
     * @param group
     * @param tag
     * @return
     */
    public boolean isActive(TagGroupBean group, TagBean tag) {
        if (bean.getTagList().isEmpty()) {
            bean.setTagList(new ArrayList<>(5));
        }
        return bean.getTagList().contains(tag.getCode());
    }

    /**
     * 选择标签
     *
     * @param groupBean
     * @param tagBean
     */
    public void selectTag(TagGroupBean groupBean, TagBean tagBean) {
        if (bean.getTagList() == null) {// 判断标签列表是否为空
            bean.setTagList(new ArrayList<>(5));
        }
        if (groupBean.getRepeatable() == 0) {
            for (TagBean tag : groupBean.getChildren()) {
                if (!Objects.equals(tagBean.getCode(), tag.getCode())) {
                    bean.getTagList().remove(tag.getCode());
                }
            }
        }
        if (bean.getTagList().contains(tagBean.getCode())) {
            bean.getTagList().remove(tagBean.getCode());
        } else {
            bean.getTagList().add(tagBean.getCode());
        }
    }

    /**
     * 保存资源标签
     */
    public void saveTypeTags() {
        StringBuffer stringBuffer = new StringBuffer();
        List<Integer> lists = bean.getTagList();
        for (int i = 0; i < lists.size(); i++) {
            stringBuffer.append(lists.get(i));
            if (lists.size() != (i + 1)) {
                stringBuffer.append(",");
            }
        }
        bean.setRes_tags(stringBuffer.toString());
        int r = TypedBusiness.getInstance().saveTypeTags(bean);
        addTip(r);
        tab(EnumValue.SectionTyped.TAG, bean);
    }

    public List<TagGroupBean> getGroups() {
        return groups;
    }

    public void setGroups(List<TagGroupBean> groups) {
        this.groups = groups;
    }

}
