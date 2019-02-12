package hndt.radiolibs.ctrl;

import hndt.radiolibs.bean.*;
import hndt.radiolibs.biz.*;
import hndt.radiolibs.jsf.TimestampTimeConverter;
import hndt.radiolibs.util.DBTool;
import hndt.radiolibs.util.Flash;
import hndt.radiolibs.util.GSON;
import hndt.radiolibs.util.Utils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 节目单Controller
 *
 * @author Hystar
 * @date 2017/7/19
 */
@Named
@ViewScoped
public class ProgramController extends BaseController {

    private Long channel_id;
    private ProgramBean bean;

    /**
     * 用于保存选取的日期
     */
    private String dateValue;
    private String selectFormatDate;
    private String resIds;

    public ProgramController() {
        if (listView()) {
            pagination();
        } else if (createView()) {
            bean = new ProgramBean();
            bean.setManager_id(getManagerIdParam());
            // 填充所属频率下拉框
            ProgramBusiness.getInstance().attachChannelBeanList(bean);
        } else if (editView()) {
            bean = ProgramBusiness.getInstance().load(id);

            if (StringUtils.isNotEmpty(bean.getDays())) {
                bean.setSelectDates(GSON.toList(bean.getDays(), Utils.typeInteger));
            }
            if (StringUtils.isNotEmpty(bean.getClock_ids())) {
                bean.setClockIds(GSON.toList(bean.getClock_ids(), Long.class));
                bean.setClockIdStr(Utils.asString(bean.getClockIds()));
            }
            // 填充所属频率下拉框
            ProgramBusiness.getInstance().attachChannelBeanList(bean);
            ProgramBusiness.getInstance().attachOrderClockIdsList(bean);

        } else if (previewView()) {
            bean = ProgramBusiness.getInstance().load(id);
            channel_id = bean.getChannel_id();
            // 设置节目单的播放时段，只返回时间，不返回日期
            TimestampTimeConverter timeConverter = new TimestampTimeConverter();
            String start = timeConverter.getAsString(null, null, bean.getStarttime());
            String end = timeConverter.getAsString(null, null, bean.getEndtime());
            bean.setStartStr(start);
            bean.setEndStr(end);
            dateValue = String.valueOf(System.currentTimeMillis());
            if (getParam("selectDate") != null) {
                dateValue = getParam("selectDate");
            }
            prePreview();
        }
    }

    @PostConstruct
    public void putClockInSession() {
        putSession("program", bean);
    }

    @PreDestroy
    public void destroyClockFromSession() {
        removeFromSession("program");
    }

    /**
     * 获取节目列表并分页
     */
    @Override
    public void pagination() {
        setParamPage();
        pageBean = ProgramBusiness.getInstance().pagination(getManagerIdParam(), channel_id, keyword, pageBean);

        List<ProgramBean> programBeanList = pageBean.getList();
        if (Utils.isNotEmpty(programBeanList)) {
            // 循环programBeanList填充clockBeanList
            programBeanList.forEach(programBean -> ProgramBusiness.getInstance().attachOrderClockIdsList(programBean));
        }
    }

    /**
     * 添加或更新节目
     */
    public void save() {
        String days = null;
        if (Utils.isNotEmpty(bean.getSelectDates())) {
            days = GSON.toJson(bean.getSelectDates());
        }
        bean.setDays(days);
        bean.setClock_ids(GSON.toJson(bean.getClockIds()));

        // 表单验证
        String errorMessage = validate(bean);
        if (Utils.isNotEmpty(errorMessage)) {
            addTip(errorMessage);
        } else {
            int r = ProgramBusiness.getInstance().save(bean);
            afterAction(r);
        }
    }

    public void toggle(ProgramBean bean) {
        int r = ProgramBusiness.getInstance().toggleStatus(bean);
        afterAction(r);
    }

    public ProgramBean current() {
        ProgramBean bean = null;
        if (channel_id != null && channel_id > 0) {
            int week = LocalDate.now().getDayOfWeek().getValue();
            bean = ProgramBusiness.getInstance().load(sessionManager().getId(), channel_id, week);
        }
        return bean;
    }


    private String validate(ProgramBean bean) {
        String errorMessage = "";
        if (Utils.isEmpty(bean.getName())) {
            errorMessage = "请输入节目名称";
        } else if (Utils.isEmpty(bean.getDescription())) {
            errorMessage = "请输入节目描述";
        } else if (bean.getChannel_id() <= 0 || bean.getChannel_id() == null) {
            errorMessage = "请选择所属频率";
        } else if (bean.getSelectDates().size() == 0) {
            errorMessage = "请选择播放日期";
        } else if (Utils.isEmpty(bean.getStarttime())) {
            errorMessage = "请填写开播时间";
        } else if (Utils.isEmpty(bean.getEndtime())) {
            errorMessage = "请填写结束时间";
        } else if (bean.getStarttime().getTime() >= bean.getEndtime().getTime()) {
            errorMessage = "开始时间不能大于结束时间";
        }
        return errorMessage;
    }

    /**
     * 删除节目信息
     *
     * @param bean
     */
    public void delete(ProgramBean bean) {
        int r = ProgramBusiness.getInstance().delete(bean);
        afterAction(r);
    }

    /**
     * 跳转到编辑页面
     *
     * @param bean
     */
    public void tab(ProgramBean bean) {
        redirect("program_edit.xhtml?id=" + bean.getId());
    }

    /**
     * 跳转到预览页面
     *
     * @param bean
     */
    public void view(ProgramBean bean) {
        redirect("program_preview.xhtml?id=" + bean.getId());
    }

    /**
     * 获取选择的钟型列表
     *
     * @return
     */
    public List<ClockBean> onClockIdStrChanged() {
        List<ClockBean> orderClockBeanList;

        if (bean != null && Utils.isNotEmpty(bean.getClockIdStr())) {
            List<Long> clockIds = Utils.asListLong(bean.getClockIdStr());
            Map<Long, ClockBean> clockMap = ClockBusiness.getInstance().mapByIds(new ArrayList<>(clockIds), bean.getManager_id());
            orderClockBeanList = new ArrayList<>();
            clockIds.forEach(x -> orderClockBeanList.add(clockMap.get(x)));

            bean.setClockIds(clockIds);
            bean.setClock_ids(GSON.toJson(clockIds));
            ProgramBusiness.getInstance().attachOrderClockIdsList(bean);
        } else {// 在弹出层页面没有选择钟型，则显示之前的信息
            orderClockBeanList = bean.getClockBeanList();
        }

        return orderClockBeanList;
    }

    /**
     * 通过code类型的字符串转换成name类型的字符串
     *
     * @param days
     * @return
     */
    public String getWeekByDay(String days) {
        StringBuilder stringBuilder = new StringBuilder();
        if (StringUtils.isNotEmpty(days)) {
            List<Integer> dayList = GSON.toList(days, Integer.class);
            if (dayList.size() == EnumValue.WEEK_OF_DAY) {
                stringBuilder.append("每天");
            } else {
                for (int i = 0; i < dayList.size(); i++) {
                    stringBuilder.append(EnumValue.PlayDate.instances(dayList.get(i)).getName());
                    if (dayList.size() != (i + 1)) {
                        stringBuilder.append("、");
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 将保存的记录向上移动一条数据
     * 由于在页面中显示的是启用状态，而在后台交换数据的时候传入的为所有状态的集合，因此需要重新考虑
     * 思路：传入需要向上移动的一个钟型->获取启用状态下且有顺序的钟型列表ids->获取当前钟型在ids中的位置，目的是为了获取ids中的上一条钟型ID，用户数据交换
     * ->通过indexOf()方法分别获取当前钟型和需要交换的钟型在所有状态下的位置->通过swap方法将所有状态下的钟型集合进行数据交换
     *
     * @param clockId
     */
    public void up(long clockId) {
        // 获取当前钟型在集合中的下标
        int index;

        // 获取启用状态下且有顺序的钟型id集合ids
        ProgramBusiness.getInstance().attachOrderClockIdsList(bean);
        List<ClockBean> clockBeanList = bean.getClockBeanList();
        List<Long> ids = new ArrayList<>();
        clockBeanList.forEach(clockBean -> ids.add(clockBean.getId()));

        // 获取当前钟型在ids中所在的位置
        index = ids.indexOf(clockId);
        // 获取ids中当前钟型的上一条钟型id
        Long exchangeId = ids.get(index - 1);

        // 用于填充clockIdStr属性
        Collections.swap(ids, index, ids.indexOf(exchangeId));
        bean.setClockIdStr(Utils.asString(ids));

        // 获取当前钟型在所有状态下的位置
        index = bean.getClockIds().indexOf(clockId);
        // 获取需要交换的数据在所有状态下的位置
        int pre = bean.getClockIds().indexOf(exchangeId);

        if (index == 0) {
            addTip("已经是第一个，无法移动");
        } else {
            Collections.swap(bean.getClockIds(), index, pre);
        }

        bean.setClock_ids(GSON.toJson(bean.getClockIds()));
        ProgramBusiness.getInstance().attachOrderClockIdsList(bean);
    }

    /**
     * 将保存的记录向下移动一条数据
     *
     * @param clockId
     */
    public void down(long clockId) {
        // 获取当前钟型在集合中的下标
        int index;

        ProgramBusiness.getInstance().attachOrderClockIdsList(bean);
        List<ClockBean> clockBeanList = bean.getClockBeanList();
        List<Long> ids = new ArrayList<>();
        clockBeanList.forEach(clockBean -> ids.add(clockBean.getId()));

        index = ids.indexOf(clockId);
        Long exchangeId = ids.get(index + 1);

        Collections.swap(ids, index, ids.indexOf(exchangeId));
        bean.setClockIdStr(Utils.asString(ids));

        index = bean.getClockIds().indexOf(clockId);
        int next = bean.getClockIds().indexOf(exchangeId);

        if (index == ids.size()) {
            addTip("已经是最后一个，无法移动");
        } else {
            Collections.swap(bean.getClockIds(), index, next);
        }

        bean.setClock_ids(GSON.toJson(bean.getClockIds()));
        ProgramBusiness.getInstance().attachOrderClockIdsList(bean);
    }


    public void move(ClockBean currentRow) {
        if (currentRow.getSequence() > 0) {
            bean.getClockBeanList().remove(currentRow);
            bean.getClockBeanList().add(currentRow.getSequence() - 1, currentRow);
            List<Long> ids = new ArrayList<>();
            bean.getClockBeanList().forEach(x -> {
                ids.add(x.getId());
            });
            bean.setClockIds(ids);
            bean.setClock_ids(GSON.toJson(ids));
        }
    }

    /**
     * 从ID列表中移除传入的一个id
     *
     * @param id
     */
    public void removeForClockIds(long id) {
        bean.getClockIds().remove(id);
        bean.setClock_ids(GSON.toJson(bean.getClockIds()));
        bean.setClockIdStr(Utils.asString(bean.getClockIds()));
        ProgramBusiness.getInstance().attachOrderClockIdsList(bean);
    }

    /**
     * 判断传入的ID所对应的数据是否是第一条数据
     *
     * @param clockId
     * @return
     */
    public boolean firstData(long clockId) {
        boolean isFirst = true;
        List<ClockBean> clockBeanList = bean.getClockBeanList();
        for (int i = 0; i < clockBeanList.size(); i++) {
            if (clockBeanList.get(0).getId() == clockId) {
                isFirst = false;
            }
        }
        return isFirst;
    }

    /**
     * 判断传入的ID所对应的数据是否是最后一条数据
     *
     * @param clockId
     * @return
     */
    public boolean lastData(long clockId) {
        boolean isLast = true;
        List<ClockBean> clockBeanList = bean.getClockBeanList();
        for (int i = 0; i < clockBeanList.size(); i++) {
            if (clockBeanList.get(clockBeanList.size() - 1).getId() == clockId) {
                isLast = false;
            }
        }
        return isLast;
    }

    /**
     * 获取当前节目单信息
     * <p>
     * 数据返回：返回数据为指定频率下的所有钟型信息，包括该钟型下的所有资源文件信息，其中钟型和资源文件需要按照原来的顺序排列
     * 设计思想：获取指定频率下指定时间下的runtime表中的信息，然后通过该信息填充clock_ids和res_ids，再通过clock_ids和res_ids填充List<ClockBean>
     *
     * @return
     */
    public void prePreview() {
        LocalDate selectedDate = LocalDate.now();
        bean.getClockBeanList().clear();
        if (Utils.isNotEmpty(dateValue)) {
            selectedDate = new Date(Long.parseLong(dateValue)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            selectFormatDate = Utils.DATEFORMAT3.format(selectedDate);
        }

        List<RuntimeBean> list = RuntimeBusiness.getInstance().list(channel_id, selectedDate);
        Set<Long> res_ids = new LinkedHashSet<>();

        List<Long> clockIds = GSON.toList(bean.getClock_ids(), Long.class);
        Map<Long, ClockBean> clockMap = ClockBusiness.getInstance().mapByIds(clockIds);
        list.forEach(runtimeBean -> runtimeBean.setClockBean(clockMap.get(Long.valueOf(runtimeBean.getClock_id()))));

        List<Long> typedIds = list.stream().map(RuntimeBean::getTyped_id).collect(Collectors.toList());
        Map<Long, TypedBean> typedMap = TypedBusiness.getInstance().mapByIds(typedIds);
        list.forEach(runtimeBean -> runtimeBean.setTypedBean(typedMap.get(runtimeBean.getTyped_id())));
        list.forEach(runtimeBean -> {
            if (Utils.isEmpty(Utils.isEmpty(runtimeBean.getTypedBean())) && Utils.isEmpty(runtimeBean.getTypedBean().getColor())) {
                runtimeBean.getTypedBean().setColor(EnumValue.Color.C00.getCode());
            }
        });

        list.forEach(x -> res_ids.add(x.getRes_id()));
        Map<Long, ResBean> resMap = ResBusiness.getInstance().mapByIds(new ArrayList<>(res_ids));
        list.forEach(x -> x.setResBean(resMap.get(Long.valueOf(x.getRes_id()))));
        list.forEach(x -> {
            ResBusiness.getInstance().attachCreatorList(x.getResBean());
            ResBusiness.getInstance().attachSinger(x.getResBean());
        });

        // 实现排序
        clockIds.forEach(x -> {
            if (clockMap.get(x) != null) bean.getClockBeanList().add(clockMap.get(x));
        });

        LocalTime clockStartTime = bean.getStarttime().toLocalDateTime().toLocalTime();
        LocalTime resStartTime = null;
        for (int i = 0; i < bean.getClockBeanList().size(); i++) {
            ClockBean cbean = bean.getClockBeanList().get(i);
            if (cbean == null) continue;
            resStartTime = LocalTime.ofSecondOfDay(clockStartTime.toSecondOfDay());
            cbean.setStarttime(clockStartTime.format(Utils.DATEFORMAT6));
            clockStartTime = clockStartTime.plusMinutes(cbean.getDuration());
            cbean.setEndtime(clockStartTime.format(Utils.DATEFORMAT6));
            for (RuntimeBean rbean : list) {
                if (cbean.getId().equals(rbean.getClock_id())) {
                    cbean.getRuntimeList().add(rbean);
                    rbean.setExpectedToPlay(resStartTime.format(Utils.DATEFORMAT6));
                    resStartTime = resStartTime.plusSeconds(rbean.getResBean() == null ? rbean.getDuration() : rbean.getResBean().getFormat_duration());
                    if (rbean.getResBean() == null) {
                        rbean.setResBean(new ResBean());
                        rbean.getResBean().setFormat_duration(Long.valueOf(rbean.getDuration()));
                        rbean.getResBean().setTitle_proper("预留节目");
                    }
                }
            }
        }
    }

    public void onChangeDate() {
        bean.getClockBeanList().clear();
        prePreview();
    }

    /**
     * 判断传入的List是否为空
     *
     * @param list
     * @return
     */
    public boolean isNull(List list) {
        return list.size() == 0 ? true : false;
    }

    /**
     * 通过id获取资源文件信息
     *
     * @param id
     * @return
     */
    public ResBean getResById(long id) {
        return ResBusiness.getInstance().load(id);
    }

    /**
     * 将传入的runtime表中的记录向上移动一条数据
     *
     * @param runtimeBean
     */
    public void upRes(RuntimeBean runtimeBean) {
        long tempId = runtimeBean.getSequence();
        // 获取当前记录的上一条记录
        RuntimeBean preRuntime = RuntimeBusiness.getInstance().getPreRuntimeById(runtimeBean.getManager_id(), runtimeBean.getChannel_id(), runtimeBean.getProgram_id(), runtimeBean.getClock_id(), runtimeBean.getCreatetime(), runtimeBean.getSequence());
        if (preRuntime == null) {
            addTip("无法移动，已经是第一条数据");
        } else {
            runtimeBean.setSequence(preRuntime.getSequence());
            preRuntime.setSequence(tempId);
            RuntimeBusiness.getInstance().updateSequenceById(runtimeBean.getId(), runtimeBean.getSequence());
            RuntimeBusiness.getInstance().updateSequenceById(preRuntime.getId(), preRuntime.getSequence());
        }
        redirect("program_preview.xhtml?id=" + id + "&selectDate=" + dateValue);
    }

    /**
     * 将传入的runtime表中的记录向下移动一条数据
     *
     * @param runtimeBean
     */
    public void downRes(RuntimeBean runtimeBean) {
        long tempId = runtimeBean.getSequence();
        // 获取当前记录的下一条记录
        RuntimeBean preRuntime = RuntimeBusiness.getInstance().getNextRuntimeById(runtimeBean.getManager_id(), runtimeBean.getChannel_id(), runtimeBean.getProgram_id(), runtimeBean.getClock_id(), runtimeBean.getCreatetime(), runtimeBean.getSequence());
        if (preRuntime == null) {
            addTip("无法移动，已经是最后一条数据");
        } else {
            runtimeBean.setSequence(preRuntime.getSequence());
            preRuntime.setSequence(tempId);
            RuntimeBusiness.getInstance().updateSequenceById(runtimeBean.getId(), runtimeBean.getSequence());
            RuntimeBusiness.getInstance().updateSequenceById(preRuntime.getId(), preRuntime.getSequence());
            redirect("program_preview.xhtml?id=" + id + "&selectDate=" + dateValue);
        }
    }


    /**
     * 判断传入的runtime数据是否为该钟型下的第一条数据
     *
     * @param runtimeBean
     * @return
     */
    public boolean firstRes(RuntimeBean runtimeBean) {
        RuntimeBean preRuntime = RuntimeBusiness.getInstance().getPreRuntimeById(runtimeBean.getManager_id(), runtimeBean.getChannel_id(), runtimeBean.getProgram_id(), runtimeBean.getClock_id(), runtimeBean.getCreatetime(), runtimeBean.getSequence());
        return preRuntime == null ? false : true;
    }

    /**
     * 判断传入的数据是否为该钟型下的最后一条数据
     *
     * @param runtimeBean
     * @return
     */
    public boolean lastRes(RuntimeBean runtimeBean) {
        RuntimeBean nextRuntime = RuntimeBusiness.getInstance().getNextRuntimeById(runtimeBean.getManager_id(), runtimeBean.getChannel_id(), runtimeBean.getProgram_id(), runtimeBean.getClock_id(), runtimeBean.getCreatetime(), runtimeBean.getSequence());
        return nextRuntime == null ? false : true;
    }

    //生成次日节目单，会重取文件会重新挑选音频资源并覆盖现有数据
    public void pick() {
        LocalDateTime now = LocalDateTime.now().plusDays(1);
        RuntimeBusiness.getInstance().clear(channel_id, now);
        RuntimeBusiness.getInstance().generate(channel_id, Timestamp.valueOf(now));
    }

    /**
     * 通过id删除一条runtime表中的数据
     *
     * @param id
     */
    public void removeRuntimeById(long id) {
        int r = RuntimeBusiness.getInstance().removeRuntimeById(id);
        if (r > 0) {
            addTip("信息删除成功");
            redirect("program_preview.xhtml?id=" + id + "&selectDate=" + dateValue);
        } else {
            addTip("信息删除失败");
        }
    }

    //定位到正在播放到资源
    public void findCurrentPlayItem() {
        Long currentPlayItemId = RuntimeBusiness.getInstance().currentPlayItem(channel_id);
        FacesContext.getCurrentInstance().getPartialViewContext().getEvalScripts().add("goCurrentPlayItem('" + currentPlayItemId + "')");
    }

    //判断钟型是否满
    public String clockWarning(ClockBean clock) {
        StringBuilder sb = new StringBuilder("<span class='am-center am-text-sm w200'> </span>");
        LocalDate now = LocalDate.now();
        LocalTime endtime = LocalTime.parse(clock.getEndtime());
        LocalDateTime endDateTime = LocalDateTime.of(now, endtime);
        if (endtime.toSecondOfDay() == 0) {
            endDateTime = now.atTime(23, 59, 59);
        }

        if (clock.getRuntimeList().size() > 0) {
            RuntimeBean rbean = clock.getRuntimeList().get(clock.getRuntimeList().size() - 1);
            LocalTime expTime = LocalTime.parse(rbean.getExpectedToPlay());
            long diff = 0;
            if (expTime.getHour() == 00) {
                diff = expTime.toSecondOfDay();
            } else {
                LocalDateTime endDateTime2 = LocalDateTime.of(endDateTime.toLocalDate(), expTime);
                endDateTime2 = endDateTime2.plusSeconds(rbean.getResBean().getFormat_duration());
                diff = Duration.between(endDateTime, endDateTime2).toMillis() / 1000;
            }
            LocalTime tmp = LocalTime.ofSecondOfDay(Math.abs(diff));
            if (diff > 0) {
                sb.insert(40, "<i class='am-icon-circle-o am-text-success am-text-sm'></i> 钟型已满，超出 " + tmp.format(DateTimeFormatter.ofPattern("H:m:s")));
                sb.insert(13, " am-text-default ");
            } else {
                sb.insert(40, "<i class='am-icon-circle-o-notch am-text-danger am-text-sm'></i> 钟型未满，少 " + tmp.format(DateTimeFormatter.ofPattern("H:m:s")));
                sb.insert(13, " am-text-danger ");
            }
        }
        return sb.toString();
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
     * 保存选取的资源信息
     *
     * @param programBean
     * @param clockBean
     */
    public void saveSelectedRes(ProgramBean programBean, ClockBean clockBean) {
        if (resIds.isEmpty()) return;
        List<String> ids = Utils.asList(resIds);
        List<ResBean> resBeanList = ResBusiness.getInstance().listByIds(ids);
        for (ResBean resBean : resBeanList) {
            RuntimeBean runtimeBean = new RuntimeBean();
            // 将前台返回的String类型的时间戳转换成Timestamp类型的日期
            LocalDate d = new Date(Long.parseLong(dateValue)).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            Timestamp dateTime = Timestamp.valueOf(d.atTime(0, 0, 0));
            runtimeBean.setPlaydate(dateTime);
            runtimeBean.setTyped_id(0);
            runtimeBean.setClock_id(clockBean.getId());
            runtimeBean.setChannel_id(programBean.getChannel_id());
            runtimeBean.setManager_id(getManagerIdParam());
            runtimeBean.setProgram_id(programBean.getId());
            runtimeBean.setRes_id(resBean.getId());
            runtimeBean.setCreatetime(dateTime);
            if (RuntimeBusiness.getInstance().load(resBean.getId(), clockBean.getId()) == null) {// 判断添加的资源文件在当前钟型中是否已经存在
                RuntimeBusiness.getInstance().save(runtimeBean);
                prePreview();
            }
        }

    }

    public void afterAction(int r) {
        addTip(new Flash(r));
        if (r > 0) {
            redirect("program_list.xhtml");
        }
    }

    public ProgramBean getBean() {
        return bean;
    }

    public void setBean(ProgramBean bean) {
        this.bean = bean;
    }

    public String getDateValue() {
        return dateValue;
    }

    public void setDateValue(String dateValue) {
        this.dateValue = dateValue;
    }

    public String getResIds() {
        return resIds;
    }

    public void setResIds(String resIds) {
        this.resIds = resIds;
    }

    public String getSelectFormatDate() {
        return selectFormatDate;
    }

    public void setSelectFormatDate(String selectFormatDate) {
        this.selectFormatDate = selectFormatDate;
    }
}
