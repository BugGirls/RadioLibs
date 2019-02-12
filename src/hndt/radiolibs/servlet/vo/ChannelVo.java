package hndt.radiolibs.servlet.vo;

import hndt.radiolibs.bean.ResBean;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by Hystar on 2017/10/19.
 */
public class ChannelVo {
    Long id;
    String uuid;//作为WOWZA里的直播流名称
    String name;// 频率名称
    String description;// 频率描述
    String logo;// 频率LOGO，存放路径
    int status;// 当前状态
    ResVo shim;//垫片
    Timestamp createtime;// 创建时间
    LocalTime starttime;
    LocalTime endtime;

    List<ClockVo> clockList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResVo getShim() {
        return shim;
    }

    public void setShim(ResVo shim) {
        this.shim = shim;
    }

    public List<ClockVo> getClockList() {
        return clockList;
    }

    public void setClockList(List<ClockVo> clockList) {
        this.clockList = clockList;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public LocalTime getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalTime starttime) {
        this.starttime = starttime;
    }

    public LocalTime getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalTime endtime) {
        this.endtime = endtime;
    }
}
