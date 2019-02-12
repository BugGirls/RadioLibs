package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ColumnCreatorBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.lang3.math.NumberUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ResCreateController extends ResController {

    ColumnCreatorBean creatorBean;
    List<ColumnCreatorBean> list = new ArrayList<>();

    public ResCreateController() {
        list = bean.getCreatorList();
        if (list == null && bean.getCreator_content() != null) {
            ResBusiness.getInstance().attachCreatorList(bean);
            list = bean.getCreatorList();
        }
        if (list == null) {
            list = (new ArrayList<>());
        }
        int idx = NumberUtils.toInt(getParam("idx"), -1);
        if (idx > -1) {
            creatorBean = list.get(idx);
        } else {
            creatorBean = new ColumnCreatorBean();
        }
    }

    public void save() {
        int idx = NumberUtils.toInt(getParam("idx"), -1);
        if (idx > -1) {
            list.remove(idx);
            list.add(idx, creatorBean);
        } else {
            if (creatorBean != null && Utils.isNotEmpty(creatorBean.getName(), creatorBean.getAction())) {
                list.add(creatorBean);
            } else {
                addTip(-1, "请填写完整信息");
            }
        }
        if (idx > -1 || (creatorBean != null && Utils.isNotEmpty(creatorBean.getName(), creatorBean.getAction()))) {
            bean.setCreator_content(GSON.toJson(list));
            int r = ResBusiness.getInstance().saveCreator(bean);
            addTip(r);
            tab(bean, EnumValue.Section.CREATE);
        }
    }

    public void edit(ColumnCreatorBean row) {
        redirect("res_edit_" + EnumValue.Section.CREATE.getCode() + ".xhtml?id=" + bean.getId() + "&idx=" + list.indexOf(row));
    }

    public void delete(ColumnCreatorBean row) {
        list.remove(row);
        bean.setCreator_content(GSON.toJson(list));
        int r = ResBusiness.getInstance().saveCreator(bean);
        addTip(r);
        tab(bean, EnumValue.Section.CREATE);
    }

    public List<ColumnCreatorBean> getList() {
        return list;
    }

    public void setList(List<ColumnCreatorBean> list) {
        this.list = list;
    }

    public ColumnCreatorBean getCreatorBean() {
        return creatorBean;
    }

    public void setCreatorBean(ColumnCreatorBean creatorBean) {
        this.creatorBean = creatorBean;
    }

}


