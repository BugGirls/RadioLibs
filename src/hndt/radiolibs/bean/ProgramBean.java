package hndt.radiolibs.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 节目单，描述每一天播放的节目
 * <p>
 * @author Hystar
 * @data 2017/7/19
 */
public class ProgramBean implements Serializable {

    Long id;
    Long channel_id;// 所属频率
    Long manager_id;// 所属管理员
    String name;// 节目单名称
    String description;// 节目单描述
    String days;// 播放计划。该节目单播放的日期，0-每天 1-7表示周一到周日
    Timestamp starttime;// 开播时间
    Timestamp endtime;// 关闭播出时间，和开播时间一起组成每天的播出时间段，都为NULL时表示全天播出
    String clock_ids;// 包含的钟型列表，存放json类型的数据[1,2,3]
    Timestamp startdate; //生效开始时间
    Timestamp enddate;//生效结束时间
    int status;
    // transient
    transient List<Integer> selectDates = new ArrayList<>();// 用于存放选取的播放日期，在页面中选取播放的日期时使用
    transient List<ClockBean> clockBeanList = new ArrayList<>();// 用户存放钟型信息，在加载ClockController类是初始化这个集合
    transient List<Long> clockIds = new ArrayList<>();// 用于存放该节目单下的钟型ID
    transient List<ChannelBean> channelBeanList = new ArrayList<>();
    String clockIdStr;// 用于存放选取的钟型id,存放string字符串1,2,3
    String startStr;
    String endStr;
    LocalTime localStartTime;
    LocalTime localEndTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Long channel_id) {
        this.channel_id = channel_id;
    }

    public Long getManager_id() {
        return manager_id;
    }

    public void setManager_id(Long manager_id) {
        this.manager_id = manager_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    public String getClock_ids() {
        return clock_ids;
    }

    public void setClock_ids(String clock_ids) {
        this.clock_ids = clock_ids;
    }

    public List<Integer> getSelectDates() {
        return selectDates;
    }

    public void setSelectDates(List<Integer> selectDates) {
        this.selectDates = selectDates;
    }

    public List<ClockBean> getClockBeanList() {
        return clockBeanList;
    }

    public void setClockBeanList(List<ClockBean> clockBeanList) {
        this.clockBeanList = clockBeanList;
    }

    public List<Long> getClockIds() {
        return clockIds;
    }

    public void setClockIds(List<Long> clockIds) {
        this.clockIds = clockIds;
    }

    public String getStartStr() {
        return startStr;
    }

    public void setStartStr(String startStr) {
        this.startStr = startStr;
    }

    public String getEndStr() {
        return endStr;
    }

    public void setEndStr(String endStr) {
        this.endStr = endStr;
    }

    public List<ChannelBean> getChannelBeanList() {
        return channelBeanList;
    }

    public void setChannelBeanList(List<ChannelBean> channelBeanList) {
        this.channelBeanList = channelBeanList;
    }

    public String getClockIdStr() {
        return clockIdStr;
    }

    public void setClockIdStr(String clockIdStr) {
        this.clockIdStr = clockIdStr;
    }

    public LocalTime getLocalStartTime() {
        return localStartTime;
    }

    public void setLocalStartTime(LocalTime localStartTime) {
        this.localStartTime = localStartTime;
    }

    public LocalTime getLocalEndTime() {
        return localEndTime;
    }

    public void setLocalEndTime(LocalTime localEndTime) {
        this.localEndTime = localEndTime;
    }

    public Timestamp getStartdate() {
        return startdate;
    }

    public void setStartdate(Timestamp startdate) {
        this.startdate = startdate;
    }

    public Timestamp getEnddate() {
        return enddate;
    }

    public void setEnddate(Timestamp enddate) {
        this.enddate = enddate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
