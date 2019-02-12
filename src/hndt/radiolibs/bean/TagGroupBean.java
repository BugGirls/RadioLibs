package hndt.radiolibs.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @author Empress
 */
public class TagGroupBean implements Serializable {
    /**
     * 标签组
     */
    long id;
    /**
     * 所属Manager
     */
    long manager_id;
    /**
     * 序号
     */
    long sequence;
    /**
     * 是否启用
     */
    int status;
    /**
     * 可重复选择
     */
    int repeatable;
    /**
     * 1自然客观属性，0主观属性
     */
    int naturally;
    /**
     * 是否系统库
     */
    int system;
    /**
     * 标签组名称
     */
    String name;
    /**
     * 创建时间
     */
    Timestamp createtime;

    /**
     * transient
     */
    List<Integer> selected;
    List<TagBean> children;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getManager_id() {
        return manager_id;
    }

    public void setManager_id(long manager_id) {
        this.manager_id = manager_id;
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

    public int getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(int repeatable) {
        this.repeatable = repeatable;
    }

    public int getSystem() {
        return system;
    }

    public void setSystem(int system) {
        this.system = system;
    }

    public int getNaturally() {
        return naturally;
    }

    public void setNaturally(int naturally) {
        this.naturally = naturally;
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

    public List<Integer> getSelected() {
        return selected;
    }

    public void setSelected(List<Integer> selected) {
        this.selected = selected;
    }

    public List<TagBean> getChildren() {
        return children;
    }

    public void setChildren(List<TagBean> children) {
        this.children = children;
    }
}
