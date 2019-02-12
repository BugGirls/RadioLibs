package hndt.radiolibs.bean;

import com.google.gson.annotations.Expose;
import hndt.radiolibs.util.GSON;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 钟型
 * <p>
 * @author Hystar
 * @date 2017/7/20
 */
public class ClockBean implements Serializable {

    Long id;
    Long manager_id;// 所属管理员
    Long channel_id;// 所属频率
    String name;// 钟型名称
    int duration;// 持续时长(分钟)
    int status;// 启用状态 1-有效 0-无效
    Timestamp createtime;// 创建时间
    String typed_ids;// 存放多个规则 JSON

    // transient
    transient List<TypedBean> typedList = new ArrayList<>();
    transient List<ResBean> resList = new ArrayList<>();
    transient List<RuntimeBean> runtimeList = new ArrayList<>();
    transient List<ChannelBean> channelList = new ArrayList<>();
    String starttime;
    String endtime;
    int sequence;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public String getTyped_ids() {
        return typed_ids;
    }

    public void setTyped_ids(String typed_ids) {
        this.typed_ids = typed_ids;
    }

    public List<TypedBean> getTypedList() {
        return typedList;
    }

    public void setTypedList(List<TypedBean> typedList) {
        this.typedList = typedList;
    }

    public List<ResBean> getResList() {
        return resList;
    }

    public void setResList(List<ResBean> resList) {
        this.resList = resList;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public List<RuntimeBean> getRuntimeList() {
        return runtimeList;
    }

    public void setRuntimeList(List<RuntimeBean> runtimeList) {
        this.runtimeList = runtimeList;
    }

    public List<ChannelBean> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<ChannelBean> channelList) {
        this.channelList = channelList;
    }

    public Long getManager_id() {
        return manager_id;
    }

    public void setManager_id(Long manager_id) {
        this.manager_id = manager_id;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
