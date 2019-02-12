package hndt.radiolibs.bean;

import hndt.radiolibs.util.GSON;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 * <p>
 * Created by Hystar on 2017/7/20.
 */
public class RoleBean  implements Serializable {

    Long id;// 角色信息
    String name;// 角色名称
    String permission_ids;// 该角色所拥有的权限
    int status;// 启用状态 1有效 0无效

    //transient
    List<Long> selectedIds = new ArrayList<>();// 存放页面选择ID列表
    List<PermissionBean> permissionBeanList = new ArrayList<>();// 存放权限信息列表

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

    public String getPermission_ids() {
        return permission_ids;
    }

    public void setPermission_ids(String permission_ids) {
        this.permission_ids = permission_ids;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<PermissionBean> getPermissionBeanList() {
        return permissionBeanList;
    }

    public void setPermissionBeanList(List<PermissionBean> permissionBeanList) {
        this.permissionBeanList = permissionBeanList;
    }

    public List<Long> getSelectedIds() {
        return selectedIds;
    }

    public void setSelectedIds(List<Long> selectedIds) {
        this.selectedIds = selectedIds;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
