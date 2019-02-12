package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.*;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.util.*;
import org.apache.commons.lang3.math.NumberUtils;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Empress
 */
@Named
@ViewScoped
public class ResController extends BaseController {

    ResBean bean;
    int range = 2; //1仅系统资源、2仅用户上传的资源、3所有、4组内成员所有的
    int category = 0;
    List<TagGroupBean> tagGroups = null;
    List<Integer> selectedTags = null;
    private static String BY_MEMBER = "_by_member_list";

    public ResController() {
        category = NumberUtils.toInt(getParam("category"), 0);
        String viewId = getViewId();
        if (viewId.contains("/vlive/res_by_member_list")) {
            range = 2;
        } else if (viewId.contains("/vlive/res_list.xhtml")) {
            range = 1;
        } else if (viewId.contains("/resp/res_list")) {
            range = 3;
        } else if (viewId.contains("/vlive/res_list_search.xhtml") || viewId.contains("/vlive/res_list_select.xhtml")) {
            range = 3;
        }

        if (listView()) {
            bean = new ResBean();
            if (viewId.contains("/vlive/res_list_search.xhtml") || viewId.contains("/vlive/res_select.xhtml")) {
                tagGroups = TagGroupBusiness.getInstance().list(getManager_id());
            } else {
                tagGroups = TagGroupBusiness.getInstance().listForManager(getManager_id());
            }
            TagGroupBusiness.getInstance().attachChildren(tagGroups);
            pagination();
        } else if (editView()) {
            bean = ResBusiness.getInstance().load(id);
            CustomTagBean customTags = ResBusiness.getInstance().loadCustomTag(getManager_id(), id);
            if (customTags != null) {
                bean.setType_tags(customTags.getType_tags());
            }
        } else if (createView()) {
            bean = new ResBean();
            bean.setCategory(category > 0 ? category : EnumValue.Category.SONG.getCode());
            bean.setLifetime(EnumValue.LifeTime.LIFE_FOREVER.getCode());
            bean.setVisibility(EnumValue.Visibility.PUBLIC.getCode());
            bean.setManager_id(getManagerIdParam());
            bean.setUuid(IDGen.id());
        }
    }

    public void delete(ResBean row) {
        int r = ResBusiness.getInstance().delete(row);
        if (r > 0) {
            Path path = Paths.get(getRealPath(""), row.getPath());
            try {
                Files.deleteIfExists(path);
            } catch (Exception e) {
                Logger.error(e);
            }
        }
        pagination();
    }

    public void visibility(ResBean row, int visibility) {
        int r = ResBusiness.getInstance().visibility(row, visibility);
        afterAction(r);
    }

    public void tab(ResBean resbean, EnumValue.Section section) {
        redirect("res_edit_" + section.getCode() + ".xhtml?id=" + resbean.getId());
    }


    public boolean editable(ResBean row) {
        long managerId = getManagerIdParam();
        if (managerId == row.getManager_id().longValue()) {
            return true;
        }
        if (row.getSystem() != 1) {
            //资源还是由创建者管理好一些，下面的代码暂不启用
            ManagerGroupBean group = ManagerGroupBusiness.getInstance().find(managerId);
            if (group != null) {
                List<String> leaderList = Utils.asList(group.getLeader());
                if (leaderList != null && leaderList.contains(String.valueOf(managerId))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 需要判断登录用户的角色
     * 如果是库管理员登录，则可以查看所有上传的资源文件
     * 如果不是库管理员登录，只能查看自己上传的资源文件
     */
    @Override
    public void pagination() {
        setParamPage();
        pageBean = ResBusiness.getInstance().pagination(category, range, getManagerIdParam(), selectedTags, keyword, pageBean);
        List<Long> res_ids = new ArrayList<>();
        pageBean.getList().forEach(x -> {
            ResBean row = (ResBean) x;
            ResBusiness.getInstance().attachCreatorList(row);
            ResBusiness.getInstance().attachSinger(row);
            ResBusiness.getInstance().attachTags(row);
            res_ids.add(row.getId());
        });
    }

    public List<ResBean> getResList() {
        return ResBusiness.getInstance().list(null, keyword, res_category);
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            if (getViewId().contains("/vlive/res_by_member_list")) {
                redirect("res_by_memberlist.xhtml?category=" + bean.getCategory());
            } else {
                redirect("res_list.xhtml?category=" + bean.getCategory());
            }
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
        selectedTags.clear();
        selectedTags.addAll(bean.getTagList());
        if (getViewId().contains("/vlive/res_by_member_list.xhtml") || getViewId().contains("/vlive/res_list_search.xhtml")) {
            pagination();
        }
    }

    public void clearSelectTags() {
        bean.getTagList().clear();
        selectedTags.clear();
    }

    public boolean isActive(TagGroupBean group, TagBean tag) {
        if (selectedTags == null) {
            selectedTags = (new ArrayList<>(5));
        }
        return selectedTags.contains(tag.getCode());
    }

    public List<TagGroupBean> getTagGroups() {
        return tagGroups;
    }

    public void setTagGroups(List<TagGroupBean> tagGroups) {
        this.tagGroups = tagGroups;
    }

    public List<Integer> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<Integer> selectedTags) {
        this.selectedTags = selectedTags;
    }

    public boolean tabIsActive(String section) {
        return getViewId().contains(section);
    }

    public ResBean getBean() {
        return bean;
    }

    public void setBean(ResBean bean) {
        this.bean = bean;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

}
