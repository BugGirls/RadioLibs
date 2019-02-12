package hndt.radiolibs.servlet.vo;

import hndt.radiolibs.servlet.vo.*;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClockVo {
    Long id;
    Long manager_id;// 所属管理员
    Long channel_id;// 所属频率
    String name;// 钟型名称
    int duration;// 持续时长(分钟)
    int status;// 启用状态 1-有效 0-无效
    Timestamp createtime;// 创建时间
    String typed_ids;// 存放多个规则 JSON

    LocalTime start;
    LocalTime end;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClockVo clockVo = (ClockVo) o;
        return Objects.equals(id, clockVo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    List<ResVo> playlist = new ArrayList<>();

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

    public List<ResVo> getPlaylist() {
        return playlist;
    }

    public void setPlaylist(List<ResVo> playlist) {
        this.playlist = playlist;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }
}
