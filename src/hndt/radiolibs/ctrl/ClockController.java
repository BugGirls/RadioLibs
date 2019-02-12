package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.ClockBean;
import hndt.radiolibs.bean.ProgramBean;
import hndt.radiolibs.bean.TypedBean;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 钟型Controller
 * <p>
 *
 * @author Hystar
 * @date 2017/7/20
 */
@Named
@ViewScoped
public class ClockController extends BaseController {

    private ClockBean bean;
    ProgramBean program;

    public ClockController() {
        if (listView()) {
            // 如果路径中传入的channel_id,则获取channel_id
            String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("channel_id");
            if (Utils.isNotEmpty(id)) {
                channel_id = Long.parseLong(id);
            }
            pagination();
        } else if (createView()) {
            bean = new ClockBean();
            bean.setCreatetime(new Timestamp(System.currentTimeMillis()));
            bean.setStatus(EnumValue.Status.EFFECTIVE.getCode());
            bean.setDuration(EnumValue.Duration.HOUR.getCode());
            bean.setManager_id(manager_id);
            ClockBusiness.getInstance().attachChannelBeanList(bean);
        } else if (editView()) {
            bean = ClockBusiness.getInstance().load(id);
            ClockBusiness.getInstance().attachChannelBeanList(bean);
            ClockBusiness.getInstance().attachTypedBeanList(bean);
            List<TypedBean> typedBeanList = bean.getTypedList();
            typedBeanList.forEach(typedBean -> {
                TypedBusiness.getInstance().attachTagBeanList(typedBean);
            });
            List<Long> ids = GSON.toList(bean.getTyped_ids(), Utils.typeLong);
            bean.setTyped_ids(Utils.asString(ids));
        }
        program = (ProgramBean) fromSession("program");
    }

    @PostConstruct
    public void putClockInSession() {
        putSession("clock", bean);
    }

    @PreDestroy
    public void destroyClockFromSession() {
        removeFromSession("clock");
    }

    /**
     * 获取钟型列表并分页
     *
     * @return
     */
    @Override
    public void pagination() {
        setParamPage();
        pageBean = ClockBusiness.getInstance().pagination(getManagerIdParam(), keyword, channel_id, duration, pageBean);
    }

    /**
     * 获取钟型中存放的规则列表
     *
     * @return
     */
    public List<TypedBean> getCheckTypedList() {
        List<TypedBean> typedBeanList = new ArrayList<>();
        if (Utils.isNotEmpty(bean.getTyped_ids())) {
            List<Long> integerList = Utils.stringToList(bean.getTyped_ids());
            for (Long typeid : integerList) {
                TypedBean typed = TypedBusiness.getInstance().load(typeid);
                typedBeanList.add(typed);
            }
            typedBeanList.forEach(typedBean -> TypedBusiness.getInstance().attachTagBeanList(typedBean));
            bean.setTypedList(typedBeanList);
        } else {// 为空，说明没有选择规则
            typedBeanList = bean.getTypedList();
        }
        return typedBeanList;
    }

    /**
     * 在钟型选取规则页面移除已经选取的规则
     *
     * @param typedBeanTemp
     */
    public void removeSelectedTyped(TypedBean typedBeanTemp) {
        if (bean != null && typedBeanTemp != null) {
            bean.getTypedList().remove(typedBeanTemp);
            List<Long> ids = new ArrayList<>();
            bean.getTypedList().forEach(typedBean -> ids.add(typedBean.getId()));
            bean.setTyped_ids(Utils.asString(ids));
        }
    }

    /**
     * 复制一个钟型
     *
     * @param bean
     */
    public void copy(ClockBean bean) {
        if (bean != null) {
            bean.setName(bean.getName() + "（复制）");
            bean.setManager_id(getManagerIdParam());
            int r = ClockBusiness.getInstance().copy(bean);
            if (r > 0) {
                addTip(r);
                redirect("clock_list.xhtml?channel_id=" + channel_id + "&duration=" + duration + "&keyword=" + keyword);
            }
        }
    }

    /**
     * 清空该钟型的数据
     *
     * @param bean
     */
    public void clean(ClockBean bean) {
        int r = ClockBusiness.getInstance().clean(bean);
        if (r > 0) {
            addTip(r);
            redirect("clock_edit.xhtml?id=" + bean.getId() + "&channel_id=" + channel_id + "&duration=" + duration + "&keyword=" + keyword);
        }
    }

    /**
     * 添加或更新钟型
     */
    public void save() {
        if (bean.getTypedList() != null && bean.getTypedList().size() > 0) {
            List<Long> typed_ids = new ArrayList<>();
            bean.getTypedList().stream().mapToLong(x -> x.getId()).forEach(x -> typed_ids.add(x));
            bean.setTyped_ids(GSON.toJson(typed_ids));
        }
        String errorMessage = validate(bean);
        if (Utils.isNotEmpty(errorMessage)) {
            addTip(errorMessage);
        } else {
            int r = ClockBusiness.getInstance().save(bean);
            addTip(r);
            redirect("clock_list.xhtml?channel_id=" + channel_id + "&duration=" + duration + "&keyword=" + keyword);
        }
    }

    public Map<String, Integer> pie() {
        Map<String, Integer> data = new LinkedHashMap<>();
        List<TypedBean> typedList = bean.getTypedList();
        TypedBean preTypeBean = null;
        for (TypedBean row : typedList) {

        }
        return data;
    }

    private String validate(ClockBean bean) {
        String errorMessage = "";
        if (Utils.isEmpty(bean.getName())) {
            errorMessage = "请输入钟型名称";
        } else if (Utils.isEmpty(bean.getDuration())) {
            errorMessage = "请输入持续时长";
        } else if (Utils.isEmpty(bean.getTyped_ids())) {
            errorMessage = "请选取规则";
        }
        return errorMessage;
    }

    /**
     * 删除钟型
     *
     * @param bean
     */
    public void delete(ClockBean bean) {
        int r = ClockBusiness.getInstance().delete(bean);
        afterAction(r);
    }

    /**
     * 跳转到编辑页面
     *
     * @param bean
     */
    public void tab(ClockBean bean) {
        redirect("clock_edit.xhtml?id=" + bean.getId() + "&channel_id=" + channel_id + "&duration=" + duration + "&keyword=" + keyword);
    }


    public void up(TypedBean currentRow) {
        int idx = bean.getTypedList().indexOf(currentRow);
        if (idx > 0) {
            Collections.swap(bean.getTypedList(), idx, idx - 1);
        }
    }

    public void down(TypedBean currentRow) {
        int idx = bean.getTypedList().indexOf(currentRow);
        if (idx < bean.getTypedList().size() - 1) {
            Collections.swap(bean.getTypedList(), idx, idx + 1);
        }
    }

    public void move(TypedBean currentRow) {
        if (currentRow.getSequence() > 0) {
            bean.getTypedList().remove(currentRow);
            bean.getTypedList().add(currentRow.getSequence() - 1, currentRow);
        }
    }


    /**
     * 启用与禁用
     *
     * @param row
     */
    public void toggle(ClockBean row) {
        int r = ClockBusiness.getInstance().toggleStatus(row);
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

    /**
     * 获取未被选取的钟型列表
     *
     * @return
     */
    public List<ClockBean> getClockList() {
        List<ClockBean> list = ClockBusiness.getInstance().list(getManagerIdParam(), keyword, channel_id, duration);
        return list;
    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("clock_list.xhtml?channel_id=" + channel_id + "&duration=" + duration + "&keyword=" + keyword);
        }
    }

    public ClockBean getBean() {
        return bean;
    }

    public void setBean(ClockBean bean) {
        this.bean = bean;
    }

}
