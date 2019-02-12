/**
 * author: pysh@163.com
 */
package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.*;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Logger;
import hndt.radiolibs.util.Utils;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.nio.channels.Channel;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Named("common")
@RequestScoped
public class CommonController implements Serializable {

    public List fromEnum(String name) {
        List list = Collections.EMPTY_LIST;
        if ("Category".equals(name)) {
            list = Arrays.asList(EnumValue.Category.values());
        } else if ("Visibility".equals(name)) {
            list = Arrays.asList(EnumValue.Visibility.values());
        } else if ("LifeTime".equals(name)) {
            list = Arrays.asList(EnumValue.LifeTime.values());
        } else if ("YesNo".equals(name)) {
            list = Arrays.asList(EnumValue.YesNo.values());
        } else if ("Section".equals(name)) {
            list = Arrays.asList(EnumValue.Section.values());
        } else if ("Quality".equals(name)) {
            list = Arrays.asList(EnumValue.Quality.values());
        } else if ("Role".equals(name)) {
            list = Arrays.asList(EnumValue.Role.values());
        } else if ("Status".equals(name)) {
            list = Arrays.asList(EnumValue.Status.values());
        } else if ("Ordinal".equals(name)) {
            list = Arrays.asList(EnumValue.Ordinal.values());
        } else if ("SectionTyped".equals(name)) {
            list = Arrays.asList(EnumValue.SectionTyped.values());
        } else if ("PlayDate".equals(name)) {
            list = Arrays.asList(EnumValue.PlayDate.values());
        } else if ("Duration".equals(name)) {
            list = Arrays.asList(EnumValue.Duration.values());
        } else if ("Color".equals(name)) {
            list = Arrays.asList(EnumValue.Color.values());
        }
        return list;
    }

    public String enumLabel(String name, Object code) {
        String label = null;
        if ("Category".equals(name)) {
            label = EnumValue.Category.instances((int) code).getName();
        } else if ("Visibility".equals(name)) {
            label = EnumValue.Visibility.instances((char) code).getName();
        } else if ("LifeTime".equals(name)) {
            label = EnumValue.LifeTime.instances((char) code).getName();
        } else if ("YesNo".equals(name)) {
            label = EnumValue.YesNo.instances((int) code).getName();
        } else if ("Quality".equals(name)) {
            label = EnumValue.Quality.instances((int) code).getName();
        } else if ("Role".equals(name)) {
            label = EnumValue.Role.instances((int) code).getName();
        }
        return label;
    }

    public List fromTagGroup(String group_name) {
        List<TagBean> list = Collections.emptyList();
        TagGroupBean gbean = TagGroupBusiness.getInstance().load(group_name);
        if (gbean != null) {
            list = TagBusiness.getInstance().list(gbean.getId());
        }
        return list;
    }

    public List fromChannels() {
        List<ChannelBean> list = ChannelBusiness.getInstance().list(sessionManager().getId());
        return list;
    }

    public String yesnoLabel(int code) {
        return EnumValue.YesNo.instances(code).getName();
    }

    public String SpecialLabel(int code) {
        return EnumValue.Special.instances(code).getName();
    }

    public String categoryLabel(int code) {
        String s = null;
        if (Utils.isNotEmpty(code)) {
            s = EnumValue.Category.instances(code).getName();
        }
        return s;
    }

    public String visibilityLabel(int code) {
        return EnumValue.Visibility.instances(code).getName();
    }

    public String lifeLabel(int code) {
        return EnumValue.LifeTime.instances(code).getName();
    }

    public String sectionLabel(String code) {
        return EnumValue.Section.instances(code).getName();
    }

    public String roleLabel(int code) {
        return EnumValue.Role.instances(code).getName();
    }

    public ManagerBean manager(long id) {
        ManagerBean managerBean = ManagerBusiness.getInstance().load(id);
        if (managerBean == null) {
            managerBean = new ManagerBean();
        }
        return managerBean;
    }

    public String asString(List list, String property) {
        List<String> values = new ArrayList<>();
        list = Optional.ofNullable(list).orElse(Collections.EMPTY_LIST);
        try {
            for (Object o : list) {
                String value = BeanUtilsBean2.getInstance().getProperty(o, property);
                if (Utils.isNotEmpty(value)) {
                    values.add(value);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return Utils.asString(values);
    }

    public String typedAsDescription(TypedBean typedBean) {
        StringBuffer stringBuffer = new StringBuffer();
        List<TagBean> tagBeanList = typedBean.getTagBeanList();
        for (TagBean tagBean : tagBeanList) {
            if (typedBean != null) {
                stringBuffer.append("<span class='am-badge am-badge-success am-radius am-margin-right-xs am-margin-bottom-xs'>" + tagBean.getName() + "</span>");
            }
        }
        return stringBuffer.toString();
    }

    public String clockAsDescription(ClockBean clockBean) {
        StringBuffer sb = new StringBuffer();

        if (Utils.isNotEmpty(clockBean.getTyped_ids())) {
            List<Long> ids = GSON.toList(clockBean.getTyped_ids(), Utils.typeLong);
            List<TypedBean> typedBeanList = new ArrayList<>(ids.size());
            Map<Long, TypedBean> map = TypedBusiness.getInstance().mapByIds(ids);
            ids.stream().forEach(x -> typedBeanList.add(map.get(x)));
            for (TypedBean typedBean : typedBeanList) {
                if (typedBean != null) {
                    sb.append("<div class='typed' style='width:100%'>");
                    sb.append("<span class='am-badge am-text-warning' style='color:" + typedBean.getColor() + "'>" + typedBean.getId() + "</span>");
                    if (typedBean.getRes_category() > 0) {
                        sb.append("<span class='am-badge am-badge-primary am-radius am-margin-xs'>" + categoryLabel(typedBean.getRes_category()) + "</span>");
                    }
                    sb.append("<span class='am-badge am-badge-primary am-radius am-margin-xs'>" + ordinal(typedBean.getOrdinal()) + "</span>");
                    sb.append("<span class='am-badge am-badge-primary am-radius am-margin-xs'>" + intervals(typedBean.getIntervals()) + "</span>");
                    sb.append("<span class='am-badge am-badge-secondary am-radius am-margin-xs'>" + typedBean.getAmount() + "</span>");
                    if (typedBean.getPlaceholder() == 1) {
                        sb.append("<span class='am-badge am-badge-warning am-radius am-margin-xs'>预留</span>");
                    }
                    if (typedBean.getUnitary() == 1) {
                        sb.append("<span class='am-badge am-badge-warning am-radius am-margin-xs'>保持完整</span>");
                    }
                    if (typedBean.getRes_tags() != null) {
                        String[] res_tags = typedBean.getRes_tags().split(",");
                        List<TagBean> tagBeanList = TagBusiness.getInstance().listByCodes(GSON.toList(GSON.toJson(res_tags), Utils.typeLong));
                        for (int i = 0; i < tagBeanList.size(); i++) {
                            TagBean tagBean = tagBeanList.get(i);
                            sb.append("<span class='am-badge am-badge-success am-radius am-margin-right-xs'>" + tagBean.getName());
                            sb.append("</span>");
                        }
                    }
                    sb.append("</div>");
                }
            }
        }

        return sb.toString();
    }

    public String resTagAsDescription(ResBean resBean) {
        StringBuffer sb = new StringBuffer();
        List<TagBean> tagBeanList = resBean.getTagBeanList();
        if (tagBeanList != null) {
            for (TagBean tagBean : tagBeanList) {
                if (tagBean != null) {
                    sb.append("<span class='am-badge am-badge-success am-radius am-margin-right-xs'>" + tagBean.getName());
                    sb.append("</span>");
                }
            }
        }
        return sb.toString();
    }

    public <T, S> List<Map.Entry<T, S>> mapToList(Map<T, S> map) {
        if (map == null) {
            return null;
        }
        List<Map.Entry<T, S>> list = new ArrayList<Map.Entry<T, S>>();
        list.addAll(map.entrySet());
        return list;
    }


    public String json(Object obj) {
        return GSON.toJson(obj);
    }

    public List castToList(Object o) {
        if (o instanceof List) {
            return (List) o;
        }
        return null;
    }

    public long now() {
        return System.currentTimeMillis();
    }



    public String statusLabel(int code) {
        return EnumValue.Status.instances(code).getName();
    }

    public ChannelBean channel(long id) {
        if (id == EnumValue.ChannelId.ALL_CHANNEL.getCode()) {
            ChannelBean channelBean = new ChannelBean();
            channelBean.setName("所有频率");
            return channelBean;
        } else {
            ChannelBean channelBean = ChannelBusiness.getInstance().load(id);
            if (channelBean == null) {
                channelBean = new ChannelBean();
                channelBean.setName("未知");
            }
            return channelBean;
        }
    }

    public String ordinal(int code) {
        return EnumValue.Ordinal.instances(code).getName();
    }

    public String intervals(int code) {
        if (code == -1 || code == 0) {
            return EnumValue.Intervals.instances(code).getName();
        } else {
            return code + EnumValue.Intervals.instances(1).getName();
        }
    }

    public String days(String code) {
        return EnumValue.PlayDate.instances(Integer.parseInt(code)).getName();
    }

    public String duration(int seconds) {
        String time = String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60));
        return time;
    }

    public String fileSize(int size) {
        return FileUtils.byteCountToDisplaySize(size);
    }

    public ManagerBean sessionManager() {
        ManagerBean bean = null;
        Object obj = FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(EnumValue.LITERAL_MANAGER);
        if (obj instanceof ManagerBean) {
            bean = (ManagerBean) obj;
        }
        return bean;
    }

    public boolean localhost() {
        HttpServletRequest request = (((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()));
        String uri = request.getRequestURL().toString();
        return uri.contains("localhost") || uri.contains("127.0.0.1") || uri.contains("192.168.9.35");
    }

    public List<String> hours() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            list.add(StringUtils.leftPad(String.valueOf(i), 2, '0'));
        }
        return list;
    }

    public String hour(String str) {
        return StringUtils.left(str,2);
    }


}
