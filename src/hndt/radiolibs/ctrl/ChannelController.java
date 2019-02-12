package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ChannelBean;
import hndt.radiolibs.biz.ChannelBusiness;
import hndt.radiolibs.biz.ClockBusiness;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.IDGen;
import hndt.radiolibs.util.Utils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.sql.Timestamp;
import java.util.*;

/**
 * 网络广播频率Controller
 *
 * @author Hystar
 * @date 2017/7/17
 */

@Named
@ViewScoped
public class ChannelController extends BaseController {

    private ChannelBean bean;

    public ChannelController() {
        if (listView()) {
            pagination();
        } else if (createView()) {
            bean = new ChannelBean();
            bean.setStatus(EnumValue.Status.EFFECTIVE.getCode());
            bean.setUuid(IDGen.id());
            bean.setManager_id(getManagerIdParam());
            bean.setCreatetime(new Timestamp(System.currentTimeMillis()));
        } else if (editView()) {
            bean = ChannelBusiness.getInstance().load(id);
        }
    }

    /**
     * 执行查询并分页
     */
    @Override
    public void pagination() {
        pageBean = ChannelBusiness.getInstance().pagination(getManagerIdParam(), keyword, pageBean);
    }

    /**
     * 执行添加或更新
     */
    public void save() {
        // 表单验证
        String errorMessage = validate(bean);

        if (Utils.isNotEmpty(errorMessage)) {
            addTip(errorMessage);
        } else {
            if (bean.getPart() != null && bean.getPart().getSize() > 0) {
                //图片上传
                String logoUrl = ChannelBusiness.getInstance().logoUpload(bean);
                bean.setLogo(logoUrl);
            }
            int r = ChannelBusiness.getInstance().save(bean);
            addTip(r);
            redirect("channel_list.xhtml");
        }
    }

    /**
     * 表单验证
     *
     * @param bean
     * @return
     */
    private String validate(ChannelBean bean) {
        String errorMessage = null;
        if (Utils.isEmpty(bean.getName())) {
            errorMessage = "请输入频率名称";
        } else if (Utils.isEmpty(bean.getDescription())) {
            errorMessage = "请输入频率描述";
        } else if ((bean.getPart() == null || bean.getPart().getSize() == 0) && (bean.getLogo() == null || ("").equals(bean.getLogo()))) {
            errorMessage = "请添加一张logo";
        }
        return errorMessage;
    }

    /**
     * 执行删除操作
     *
     * @param bean
     */
    public void delete(ChannelBean bean) {
        int r = ChannelBusiness.getInstance().delete(bean);
        afterAction(r);
    }

    /**
     * 跳转到编辑页面
     *
     * @param bean
     */
    public void tab(ChannelBean bean) {
        redirect("channel_edit.xhtml?id=" + bean.getId());
    }

    /**
     * 跳转到钟型列表页，并自动筛选出该频率下的钟型
     *
     * @param bean
     */
    public void tabClock(ChannelBean bean) {
        redirect("clock_list.xhtml?channel_id=" + bean.getId());
    }


    public void tabProgram(ChannelBean bean) {
        redirect("program_list.xhtml?manager_id=" + bean.getManager_id()+"&channel_id="+bean.getId());
    }

    /**
     * 启用禁用
     *
     * @param bean
     */
    public void toggle(ChannelBean bean) {
        int r = ChannelBusiness.getInstance().toggleStatus(bean);
        afterAction(r);
    }

    /**
     * 分页操作
     *
     * @param page
     */
    @Override
    public void toPage(int page) {
        pageBean.setPage(page);
        pagination();
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("channel_list.xhtml");
        }
    }

    public ChannelBean getBean() {
        return bean;
    }

    public void setBean(ChannelBean bean) {
        this.bean = bean;
    }

}
