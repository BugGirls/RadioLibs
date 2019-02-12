package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.bean.ClockBean;
import hndt.radiolibs.bean.TypedBean;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.util.Utils;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Hystar on 2017/7/21.
 */
@Named
@ViewScoped
public class TypedController extends BaseController {

    TypedBean bean;
    List<ChannelBean> channels = new ArrayList<>();
    ClockBean clock;

    public TypedController() {
        if (listView()) {
            pagination();
        } else if (createView()) {
            bean = new TypedBean();
            bean.setRes_category(3);
            bean.setAmount(1);
            bean.setStatus(EnumValue.Status.EFFECTIVE.getCode());
            bean.setManager_id(manager_id);
            bean.setIntervals(EnumValue.Intervals.WITHOUT_REPETITION.getCode());// 不重复
            bean.setCreatetime(new Timestamp(System.currentTimeMillis()));
            bean.setSpecial(EnumValue.Special.COMMON.getCode());// 普通规则
            channels = ChannelBusiness.getInstance().list(manager_id);
        } else if (editView()) {
            bean = TypedBusiness.getInstance().load(id);
            channels = ChannelBusiness.getInstance().list(manager_id);
        }
        clock = (ClockBean) fromSession("clock");
    }

    /**
     * 通过关键字获取数据列表并封装到PageBean中实现分页
     */
    @Override
    public void pagination() {
        setParamPage();
        pageBean = TypedBusiness.getInstance().pagination(getManagerIdParam(), keyword, channel_id, res_category, pageBean);
        for (Object o : pageBean.getList()) {
            TypedBusiness.getInstance().attachTagBeanList((TypedBean)o);
        }
    }

    /**
     * 获取未被选取的规则信息列表
     *
     * @return
     */
    public List<TypedBean> getTypedList() {
        List<TypedBean> beans = TypedBusiness.getInstance().list(getManagerIdParam(), channel_id, res_category);
        beans.forEach(x->TypedBusiness.getInstance().attachTagBeanList(x));
        return beans;
    }

    /**
     * 添加或更新操作
     */
    public void save() {
        String errorMessage = validate(bean);// 前台数据验证
        if (Utils.isNotEmpty(errorMessage)) {
            addTip(errorMessage);
        } else {
            bean.setSpecial(EnumValue.Special.COMMON.getCode());
            onChange();
            int r = TypedBusiness.getInstance().save(bean);
            addTip(r);
            tab(EnumValue.SectionTyped.BASIC, bean);
        }
    }

    private String validate(TypedBean bean) {
        String errorMessage = null;
        if (bean.getRes_category() == 0) {
        } else if (bean.getAmount() <= 0) {
            errorMessage = "请正确填写音频的选取个数";
        } else if (bean.getPlaceholder() == 1 && bean.getDuration() <= 0) {
            errorMessage = "请填写占位时长";
        }
        return errorMessage;
    }

    /**
     * 复制一条数据
     *
     * @param bean
     */
    public void copy(TypedBean bean) {
        int r = TypedBusiness.getInstance().copy(bean);
        addTip(r);
        redirect("typed_list.xhtml");
    }

    /**
     * 重定向页面
     *
     * @param sectionTyped
     * @param bean
     */
    public void tab(EnumValue.SectionTyped sectionTyped, TypedBean bean) {
        redirect("typed_edit_" + sectionTyped.getCode() + ".xhtml?id=" + bean.getId());
    }

    /**
     * 删除规则
     *
     * @param bean
     */
    public void delete(TypedBean bean) {
        int r = TypedBusiness.getInstance().delete(bean);
        addTip(r);
        redirect("typed_list.xhtml");
    }


    public void onChange() {
        if (bean.getPlaceholder() == 1) {
            bean.setOrdinal(EnumValue.Ordinal.DESC.getCode());
            bean.setUnitary(1);
            bean.setAmount(1);
            if (bean.getDuration()<=0) {
                bean.setDuration(30);
            }
        }
        if (bean.getOrdinal() != EnumValue.Ordinal.RANDOM.getCode()) {
            bean.setIntervals(EnumValue.Intervals.WITHOUT_REPETITION.getCode());
        }
    }

    public void resList(TypedBean row) {
        redirect("typed_res_list.xhtml?id=" + row.getId());
    }

    /**
     * 分页操作
     *
     * @param page
     */
    public void toPage(int page) {
        pageBean.setPage(page);
        pagination();
    }

    /**
     * 判断是否为特殊规则
     *
     * @return
     */
    public boolean isSpecial(TypedBean bean) {
        return bean.getSpecial() == EnumValue.Special.SPECIAL.getCode() ? false : true;
    }

    public boolean tabIsActive(String sectionType) {
        return getViewId().contains(sectionType);
    }

    public void allCategory() {
        bean.setRes_category(-1);
    }

    public TypedBean getBean() {
        return bean;
    }

    public void setBean(TypedBean bean) {
        this.bean = bean;
    }

    public List<ChannelBean> getChannels() {
        return channels;
    }

    public void setChannels(List<ChannelBean> channels) {
        this.channels = channels;
    }

}
