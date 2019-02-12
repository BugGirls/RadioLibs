package hndt.radiolibs.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 角色功能权限
 *
 * Created by Hystar on 2017/8/4.
 */
public class PermissionBean implements Serializable {

    Long id;// 角色功能权限
    String name;// 权限名称
    String urls;// 权限地址
    int status;// 启用状态 1有效 0无效

    //transient
    List<String> urlList;

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

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }
}
