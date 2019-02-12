package hndt.radiolibs.bean;

import hndt.radiolibs.util.GSON;

import javax.servlet.http.Part;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.List;

/**
 * 频率
 * <p>
 * @author girl
 * @date 2017/7/17
 */
public class ChannelBean implements Serializable {
    Long id;
    /**
     * uuid
     */
    String uuid;
    /**
     * 所属管理员
     */
    Long manager_id;
    /**
     * 频率名称
     */
    String name;
    /**
     * 频率描述
     */
    String description;
    /**
     * 频率LOGO，存放路径
     */
    String logo;
    /**
     * 当前状态
     */
    int status;
    /**
     * 创建时间
     */
    Timestamp createtime;

    // transient
    /**
     * 存储上传的文件信息
     */
    Part part;
    /**
     * 开播时间
     */
    LocalTime starttime;
    /**
     * 关闭播出时间，和开播时间一起组成每天的播出时间段，都为NULL时表示全天播出
     */
    LocalTime endtime;

    //transient
    ResBean shim;

    public Part getPart() {
        return part;
    }

    public void setPart(Part part) {
        this.part = part;
    }

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

    public ResBean getShim() {
        return shim;
    }

    public void setShim(ResBean shim) {
        this.shim = shim;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
