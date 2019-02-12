package hndt.radiolibs.bean;

import java.sql.Timestamp;

/**
 * Created by Hystar on 2017/7/28.
 */
public class CustomTagBean {
    Long id;// 用户对音频自定义的标签
    Long manager_id;// 频率管理员
    Long res_id;// 音频ID
    String type_tags;// 自定义标签
    Timestamp updatetime; //更新时间

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

    public Long getRes_id() {
        return res_id;
    }

    public void setRes_id(Long res_id) {
        this.res_id = res_id;
    }

    public String getType_tags() {
        return type_tags;
    }

    public void setType_tags(String type_tags) {
        this.type_tags = type_tags;
    }

    public Timestamp getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Timestamp updatetime) {
        this.updatetime = updatetime;
    }
}
