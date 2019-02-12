/**
 * author: pysh@163.com
 */
package hndt.radiolibs.ctrl;

import hndt.radiolibs.biz.ManagerBusiness;
import hndt.radiolibs.util.PageBean;
import org.apache.commons.lang3.math.NumberUtils;
import hndt.radiolibs.bean.ManagerBean;
import hndt.radiolibs.biz.EnumValue;
import hndt.radiolibs.servlet.TipAnnotation;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;

public class BaseController implements Serializable {

    private static String listViewName = "_list";
    private static String editViewName = "_edit";
    private static String previewViewName = "_preview";
    private static String viewViewName = "_view";
    //闪存key
    public static final String FLASH_MESSAGE_KEY = "msg";
    //参数
    public static final String PARAM_ID = "id";
    public static final String PARAM_MANAGER_ID = "manager_id";
    public static final String PARAM_CHANNEL_ID = "channel_id";
    public static final String PARAM_DURATION = "duration";
    public static final String PARAM_KEYWORD = "keyword";
    public static final String PARAM_TYPED_ID = "typed_id";

    long id = 0;
    long manager_id = 0;
    Long channel_id = -1L;
    Long res_category = -1L;
    Long duration = -1L;
    Long typed_id = -1L;
    String keyword;
    PageBean pageBean;

    public BaseController() {
        id = getIdParam();
        manager_id = getManagerIdParam();
        channel_id = getChannelIdParam() == 0 ? -1 : getChannelIdParam();
        keyword = getKeywordParam() == null ? "" : getKeywordParam();
        duration = getDurationParam() == 0 ? -1 : getDurationParam();
    }

    public String getParam(String name) {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
        return value;
    }

    public long getIdParam() {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(PARAM_ID);
        return NumberUtils.toLong(value);
    }

    public long getDurationParam() {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(PARAM_DURATION);
        return NumberUtils.toLong(value);
    }

    public String getKeywordParam() {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(PARAM_KEYWORD);
        return value;
    }

    public long getChannelIdParam() {
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(PARAM_CHANNEL_ID);
        return NumberUtils.toLong(value);
    }

    public void setParamPage() {
        String pageParam = getParam("page");
        if (pageParam != null) {
            int page = NumberUtils.toInt(pageParam, 0);
            if (page > 0) {
                if (pageBean == null) {
                    pageBean = new PageBean();
                }
                pageBean.setPage(page);
            }
        }
    }


    /**
     * 获取前台传入的manager_id参数并返回
     *
     * @return
     */
    public long getManagerIdParam() {
        long mid = 0;
        String value = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(PARAM_MANAGER_ID);
        if (Utils.isNotEmpty(value)) {// 如果值不为空，则转换成Long类型
            mid = NumberUtils.toLong(value, 0);
        } else {// 否则获取当前登录的Manager ID
            ManagerBean mbean = sessionManager();// 获取session中的Manager
            if (mbean != null) {
                mid = sessionManager().getId();
            }
        }
        return mid;
    }

    public boolean listView() {
        return getViewId().contains(listViewName);
    }

    public boolean editView() {
        return getViewId().contains(editViewName) && (id > 0);
    }

    public boolean previewView() {
        return getViewId().contains(previewViewName) && (id > 0);
    }

    public boolean viewView() {
        return getViewId().contains(viewViewName);
    }

    public boolean createView() {
        return getViewId().contains(editViewName) && (id == 0);
    }

    public void addTip(int code, String component_id, String message) {
        if (message == null) {
            return;
        }
        if (sessionManager() != null) {
            TipAnnotation.send(sessionManager().getId(), code, message);
        }
    }

    public void addTip(int code, String message) {
        addTip(code, null, message);
    }

    public void addTip(Flash flash) {
        if (flash.getMessage() != null) {
            addTip(flash.getCode(), flash.getMessage());
        }
    }

    public void addTip(String message) {
        addTip(0, null, message);
    }

    public void addError(String message) {
        addTip(-1, null, message);
    }

    public void addTip(int r) {
        addTip(new Flash(r));
    }

    public void addFlashMessage(String message) {
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, message, null));
    }

    public Object getBean(String beanName) {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getApplication().evaluateExpressionGet(context, "#{" + beanName + "}", Object.class);
    }

    public static boolean isPostback() {
        return FacesContext.getCurrentInstance().isPostback();
    }

    public String getRealPath(String path) {
        return FacesContext.getCurrentInstance().getExternalContext().getRealPath(path);
    }

    /**
     * 获取页面的文件名
     *
     * @return
     */
    public String getViewId() {
        // 获取页面视图
        UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
        // 呈现页面页面的文件名
        return root.getViewId();
    }

    public void validate(AjaxBehaviorEvent event) {
        UIInput inputText = (UIInput) event.getSource();
        valid(inputText.getId(), inputText.getValue().toString());
    }

    public void toPage(int page) {
        pageBean.setPage(page);
        pagination();
    }

    public void pagination() {
    }


    public boolean valid(String compid, String value) {
        return true;
    }

    public void redirect(String uri) {
        try {
            if (uri.contains(".xhtml") && !uri.contains("?faces-redirect=true")) {
                uri = uri + (((uri.indexOf('?') > 0) ? "&" : "?") + "faces-redirect=true&includeViewParams=true");
                if (!uri.contains("page=") && pageBean != null && pageBean.getPage() > 0) {
                    uri = uri + "&page=" + pageBean.getPage();
                }
            }
            FacesContext.getCurrentInstance().getExternalContext().redirect(uri);
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public ManagerBean sessionManager() {
        ManagerBean bean = null;
        Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(EnumValue.LITERAL_MANAGER);
        if (obj instanceof ManagerBean) {
            bean = (ManagerBean) obj;
        }
        return bean;
    }


    public Object fromSession(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(name);
    }

    public Object putSession(String name, Object value) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(name, value);
    }

    public Object removeFromSession(String name) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(name);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getManager_id() {
        return manager_id;
    }

    public void setManager_id(long manager_id) {
        this.manager_id = manager_id;
    }

    public Long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Long channel_id) {
        this.channel_id = channel_id;
    }

    public Long getRes_category() {
        return res_category;
    }

    public void setRes_category(Long res_category) {
        this.res_category = res_category;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    public Long getTyped_id() {
        return typed_id;
    }

    public void setTyped_id(Long typed_id) {
        this.typed_id = typed_id;
    }

}
