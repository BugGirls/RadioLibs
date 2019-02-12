package hndt.radiolibs.bean;

import hndt.radiolibs.util.GSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户组
 * <p>
 * Created by Hystar on 2017/7/20.
 */
public class ManagerGroupBean implements Serializable {

    Long id;
    String name;
    String manager_ids;
    String leader;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManager_ids() {
        return manager_ids;
    }

    public void setManager_ids(String manager_ids) {
        this.manager_ids = manager_ids;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }
}
