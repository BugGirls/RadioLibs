package hndt.radiolibs.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * 对应数据库字段，resources.creator
 * <p>
 * 题名[正题名]:我的祖国妈妈 创建者[个人名称]:范竞马 [责任方式]:演唱 [责任者说明]:男高音 [个人名称]:梁上泉 [责任方式]:作词 [个人名称]:施光南 [责任方式]:作曲
 * 题名[正题名]:为你骄傲[其他题名信息]:纪念沈湘先生逝世十周年音乐会的实况录音 创建者[个人名称]:汤沐海 [责任方式]:指挥 [责任者说明]:当代世界著名指挥家，现任芬兰国家歌剧院首席指挥 [团体名称]:北京交响乐团 [责任方式]:伴奏
 */
public class ColumnCreatorBean implements Serializable {
    /**
     * 责任方式: 作词 作曲 演唱 指挥 伴奏 播音 编辑 演播 表演 监制 作者 编配
     */
    String action;
    /**
     * 个人名称\团体名称\会议名称
     */
    String name;
    /**
     * 补充信息
     */
    String info;

    public ColumnCreatorBean() {
    }

    public ColumnCreatorBean(String action, String name) {
        this.action = action;
        this.name = name;
    }

    public ColumnCreatorBean(String action, String name, String info) {
        this.action = action;
        this.name = name;
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ColumnCreatorBean that = (ColumnCreatorBean) o;
        return Objects.equals(action, that.action) &&
                Objects.equals(name, that.name) &&
                Objects.equals(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(action, name, info);
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
