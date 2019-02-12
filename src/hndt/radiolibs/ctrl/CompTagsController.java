/**
 * author: pysh@163.com
 */
package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.bean.TagBean;
import hndt.radiolibs.bean.TagGroupBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ManagerBusiness;
import hndt.radiolibs.biz.TagBusiness;
import hndt.radiolibs.biz.TagGroupBusiness;

import javax.enterprise.context.Dependent;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class CompTagsController implements Serializable {

    TagGroupBean gbean;
    List<TagBean> items;

    public void initWithGroupName(String group_name) {
        gbean = TagGroupBusiness.getInstance().load(group_name);
        gbean.setSelected(new ArrayList<>());
        items = TagBusiness.getInstance().list(gbean.getId());
    }

    public void click(TagBean row) {
        if (gbean.getRepeatable() == 0) {
            gbean.getSelected().clear();
        }
        if (gbean.getSelected().contains(row.getCode())) {
            gbean.getSelected().remove(row.getCode());
        } else {
            gbean.getSelected().add(row.getCode());
        }
    }

    public void clicked(TagBean row) {
        System.out.println(gbean.getSelected());
    }


    public boolean contains(TagBean row) {
        if (gbean.getSelected() == null) {
            return false;
        }
        return gbean.getSelected().contains(row.getCode());
    }

    public TagGroupBean getGbean() {
        return gbean;
    }

    public void setGbean(TagGroupBean gbean) {
        this.gbean = gbean;
    }

    public List<TagBean> getItems() {
        return items;
    }

    public void setItems(List<TagBean> items) {
        this.items = items;
    }
}
