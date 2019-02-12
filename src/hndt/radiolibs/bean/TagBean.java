package hndt.radiolibs.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

/**

 */
public class TagBean implements Serializable {
    long id; //ID
    long group_id;
    long sequence; //序号
    int status; //是否启用
    int wrap; //是否换行
    Integer code; //标签代码
    String name; //标签名称
    Timestamp createtime;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagBean tagBean = (TagBean) o;
        return Objects.equals(code, tagBean.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroup_id() {
        return group_id;
    }

    public void setGroup_id(long group_id) {
        this.group_id = group_id;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getWrap() {
        return wrap;
    }

    public void setWrap(int wrap) {
        this.wrap = wrap;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }
}
