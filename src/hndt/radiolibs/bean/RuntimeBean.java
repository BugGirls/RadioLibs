package hndt.radiolibs.bean;

import java.sql.Timestamp;

/**
 * 播放的资源信息
 *
 * @author Hystar
 * @date 2017/8/9
 */
public class RuntimeBean {
    /**
     * id
     */
    Long id;
    /**
     * 管理员ID
     */
    long manager_id;
    /**
     * 频率ID
     */
    long channel_id;
    /**
     * 节目单ID
     */
    long program_id;
    /**
     * 钟型ID
     */
    long clock_id;
    /**
     * 规则ID
     */
    long typed_id;
    /**
     * 资源ID
     */
    long res_id;

    int placeholder; //是否占位
    int duration;//占位时长
    int unitary;//不可截断
    /**
     * 播放日期
     */
    Timestamp playdate;

    /**
     * 创建日期
     */
    Timestamp createtime;

    long sequence;

    // transient
    /**
     * resBean
     */
    ResBean resBean;
    ClockBean clockBean;
    TypedBean typedBean;
    /**
     * 预计播放时间
     */
    String expectedToPlay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getManager_id() {
        return manager_id;
    }

    public void setManager_id(long manager_id) {
        this.manager_id = manager_id;
    }

    public long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(long channel_id) {
        this.channel_id = channel_id;
    }

    public long getProgram_id() {
        return program_id;
    }

    public void setProgram_id(long program_id) {
        this.program_id = program_id;
    }

    public long getClock_id() {
        return clock_id;
    }

    public void setClock_id(long clock_id) {
        this.clock_id = clock_id;
    }

    public long getTyped_id() {
        return typed_id;
    }

    public void setTyped_id(long typed_id) {
        this.typed_id = typed_id;
    }

    public long getRes_id() {
        return res_id;
    }

    public void setRes_id(long res_id) {
        this.res_id = res_id;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getUnitary() {
        return unitary;
    }

    public void setUnitary(int unitary) {
        this.unitary = unitary;
    }

    public Timestamp getPlaydate() {
        return playdate;
    }

    public void setPlaydate(Timestamp playdate) {
        this.playdate = playdate;
    }


    public ResBean getResBean() {
        return resBean;
    }

    public void setResBean(ResBean resBean) {
        this.resBean = resBean;
    }

    public ClockBean getClockBean() {
        return clockBean;
    }

    public void setClockBean(ClockBean clockBean) {
        this.clockBean = clockBean;
    }

    public long getSequence() {
        return sequence;
    }

    public void setSequence(long sequence) {
        this.sequence = sequence;
    }

    public String getExpectedToPlay() {
        return expectedToPlay;
    }

    public void setExpectedToPlay(String expectedToPlay) {
        this.expectedToPlay = expectedToPlay;
    }

    public TypedBean getTypedBean() {
        return typedBean;
    }

    public void setTypedBean(TypedBean typedBean) {
        this.typedBean = typedBean;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }


}
