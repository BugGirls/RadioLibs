package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ColumnRightBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.util.GSON;
import org.apache.commons.lang3.math.NumberUtils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
public class ResRightController extends ResController {
    ColumnRightBean rightBean;
    List<ColumnRightBean> list = new ArrayList<>();

    public ResRightController() {
        list = bean.getRightList();
        if (list == null && bean.getRight_content() != null) {
            ResBusiness.getInstance().attachRightList(bean);
            list = bean.getRightList();
        }
        if (list == null) {
            list = (new ArrayList<>());
        }
        int idx = NumberUtils.toInt(getParam("idx"), -1);
        if (idx > -1) {
            rightBean = list.get(idx);
        } else {
            rightBean = new ColumnRightBean();
        }
    }

    public void save() {
        int idx = NumberUtils.toInt(getParam("idx"), -1);
        if (idx > -1) {
            list.remove(idx);
            list.add(idx, rightBean);
        } else {
            if (rightBean != null) {
                list.add(rightBean);
            }
        }
        bean.setRight_content(GSON.toJson(list));
        int r = ResBusiness.getInstance().saveRight(bean);
        addTip(r);
        tab(bean, EnumValue.Section.RIGHT);
    }

    public void edit(ColumnRightBean row) {
        redirect("res_edit_" + EnumValue.Section.RIGHT.getCode() + ".xhtml?id=" + bean.getId() + "&idx=" + list.indexOf(row));
    }

    public void delete(ColumnRightBean row) {
        list.remove(row);
        bean.setRight_content(GSON.toJson(list));
        int r = ResBusiness.getInstance().saveRight(bean);
        addTip(r);
        tab(bean, EnumValue.Section.RIGHT);
    }

    public ColumnRightBean getRightBean() {
        return rightBean;
    }

    public void setRightBean(ColumnRightBean rightBean) {
        this.rightBean = rightBean;
    }

    public List<ColumnRightBean> getList() {
        return list;
    }

    public void setList(List<ColumnRightBean> list) {
        this.list = list;
    }
}


