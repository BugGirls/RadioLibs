package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.bean.ClockBean;
import hndt.radiolibs.bean.ResBean;
import hndt.radiolibs.bean.TypedBean;
import hndt.radiolibs.biz.ChannelBusiness;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.biz.ResBusiness;
import hndt.radiolibs.biz.TypedBusiness;
import hndt.radiolibs.util.PageBean;
import hndt.radiolibs.util.Utils;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hystar
 * @date 2017/7/21
 */
@Named
@ViewScoped
public class TypedResController extends BaseController {

    TypedBean bean;
    List<ResBean> list;

    public TypedResController() {
        bean = TypedBusiness.getInstance().load(getId());
        TypedBusiness.getInstance().attachTagList(bean);
        if (listView()) {
            pagination();
        }
    }

    @Override
    public void pagination() {
        setParamPage();
        list = ResBusiness.getInstance().listByTags(bean.getManager_id(),bean.getRes_category(),bean.getTagList());
    }

    public TypedBean getBean() {
        return bean;
    }

    public void setBean(TypedBean bean) {
        this.bean = bean;
    }

    public List<ResBean> getList() {
        return list;
    }

    public void setList(List<ResBean> list) {
        this.list = list;
    }
}
