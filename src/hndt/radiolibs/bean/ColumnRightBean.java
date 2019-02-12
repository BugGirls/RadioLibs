package hndt.radiolibs.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * 对应数据库字段，resources.right_content
 */
public class ColumnRightBean implements Serializable {
    String owner;//版权授权者名称
    String user; //被授权使用者名称
    String type; //权利类型, 包括广播权、信息网络传播权、摄制权、表演权、放映权、发行权、出租权、翻译权、改编权、复制权、展览权、汇编权等;
    Timestamp starttime;
    Timestamp endtime;
    String area; //授权使用地域

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnRightBean that = (ColumnRightBean) o;
        return Objects.equals(owner, that.owner) &&
                Objects.equals(user, that.user) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, user, type);
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Timestamp getStarttime() {
        return starttime;
    }

    public void setStarttime(Timestamp starttime) {
        this.starttime = starttime;
    }

    public Timestamp getEndtime() {
        return endtime;
    }

    public void setEndtime(Timestamp endtime) {
        this.endtime = endtime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
