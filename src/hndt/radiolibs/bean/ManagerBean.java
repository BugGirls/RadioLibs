package hndt.radiolibs.bean;


import hndt.radiolibs.util.GSON;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ManagerBean implements Serializable {
    Long id;//
    String name; //
    int gender;
    String icon;
    int level;
    int fans_count;
    int idol_count;
    int favorite_count;
    int praise_count;

    String role_ids; //
    String permission; //权限
    Timestamp createtime;
    int status = 1;//禁用启用

    //transient
    String selectedUserId;
    List<RoleBean> roleList = new ArrayList<>();
    List<String> urlList;// 用户能操作的url地址列表
    List<Long> selectedId = new ArrayList<>();// 用于保存复选框中选择的角色
    transient String password;

    public ManagerBean() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerBean that = (ManagerBean) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    public String getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(String selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole_ids() {
        return role_ids;
    }

    public void setRole_ids(String role_ids) {
        this.role_ids = role_ids;
    }

    public List<RoleBean> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<RoleBean> roleList) {
        this.roleList = roleList;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public List<Long> getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(List<Long> selectedId) {
        this.selectedId = selectedId;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getFans_count() {
        return fans_count;
    }

    public void setFans_count(int fans_count) {
        this.fans_count = fans_count;
    }

    public int getIdol_count() {
        return idol_count;
    }

    public void setIdol_count(int idol_count) {
        this.idol_count = idol_count;
    }

    public int getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(int favorite_count) {
        this.favorite_count = favorite_count;
    }

    public int getPraise_count() {
        return praise_count;
    }

    public void setPraise_count(int praise_count) {
        this.praise_count = praise_count;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
