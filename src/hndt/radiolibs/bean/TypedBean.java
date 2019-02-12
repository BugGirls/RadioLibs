package hndt.radiolibs.bean;

import hndt.radiolibs.util.GSON;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 规则
 * <p>
 * Created by Hystar on 2017/7/21.
 */
public class TypedBean implements Serializable {

    Long id;
    Long manager_id;// 所属管理员
    Long channel_id;// 所属频率,可为null
    int res_category;// 资源类别
    int status;// 启用状态 1-有效 0-无效
    int special;// 创建频率时是否为系统默认创建的规则 1-特殊 0-普通
    int placeholder; //占位
    int duration; //占位时长
    int unitary; //是否单元化节目
    String res_tags;// 资源的多个标签（用','分割）
    int ordinal;// 顺序策略 0-顺序 1-重播 2-随机 3-自定义
    int intervals;// 间隔策略（当顺序策略为2-随机时可用）x天内不重复 0表示无资源可选时才允许重复
    String listing;// 自定义播放节目列表，顺序策略为3-自定义时可用
    int amount;// 当前规则下对音频的选取个数
    String color; //颜色
    Timestamp createtime;// 创建时间

    transient List<Integer> tagList = Collections.EMPTY_LIST;// 存放该规则所属的标签列表
    transient List<TagBean> tagBeanList = new ArrayList<>();
    transient List<ChannelBean> channelBeanList = new ArrayList<>();
    int sequence = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getManager_id() {
        return manager_id;
    }

    public void setManager_id(Long manager_id) {
        this.manager_id = manager_id;
    }

    public Long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Long channel_id) {
        this.channel_id = channel_id;
    }

    public int getRes_category() {
        return res_category;
    }

    public void setRes_category(int res_category) {
        this.res_category = res_category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public String getRes_tags() {
        return res_tags;
    }

    public void setRes_tags(String res_tags) {
        this.res_tags = res_tags;
    }

    public int getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    public int getIntervals() {
        return intervals;
    }

    public void setIntervals(int intervals) {
        this.intervals = intervals;
    }

    public String getListing() {
        return listing;
    }

    public void setListing(String listing) {
        this.listing = listing;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public List<Integer> getTagList() {
        return tagList;
    }

    public void setTagList(List<Integer> tagList) {
        this.tagList = tagList;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getUnitary() {
        return unitary;
    }

    public void setUnitary(int unitary) {
        this.unitary = unitary;
    }

    public List<TagBean> getTagBeanList() {
        return tagBeanList;
    }

    public void setTagBeanList(List<TagBean> tagBeanList) {
        this.tagBeanList = tagBeanList;
    }

    public List<ChannelBean> getChannelBeanList() {
        return channelBeanList;
    }

    public void setChannelBeanList(List<ChannelBean> channelBeanList) {
        this.channelBeanList = channelBeanList;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
