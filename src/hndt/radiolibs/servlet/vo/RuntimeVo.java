package hndt.radiolibs.servlet.vo;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;


public class RuntimeVo implements Serializable {

    Long channel_id;
    Long clock_id;
    Long typed_id;
    Long runtime_id;
    Long res_id;
    int duration;
    int placeholder;
    int unitary;
    LocalTime starttime;

    public RuntimeVo() {
    }

    public RuntimeVo(Long channel_id, Long clock_id, Long typed_id, Long runtime_id, Long res_id) {
        this.channel_id = channel_id;
        this.clock_id = clock_id;
        this.typed_id = typed_id;
        this.runtime_id = runtime_id;
        this.res_id = res_id;
    }

    public RuntimeVo(Long channel_id, Long clock_id, Long typed_id, Long runtime_id, Long res_id, int duration, int placeholder, int unitary, LocalTime starttime) {
        this.channel_id = channel_id;
        this.clock_id = clock_id;
        this.typed_id = typed_id;
        this.runtime_id = runtime_id;
        this.res_id = res_id;
        this.duration = duration;
        this.placeholder = placeholder;
        this.unitary = unitary;
        this.starttime = starttime;
    }

    public Long getRuntime_id() {
        return runtime_id;
    }

    public void setRuntime_id(Long runtime_id) {
        this.runtime_id = runtime_id;
    }

    public Long getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(Long channel_id) {
        this.channel_id = channel_id;
    }

    public Long getClock_id() {
        return clock_id;
    }

    public void setClock_id(Long clock_id) {
        this.clock_id = clock_id;
    }

    public Long getTyped_id() {
        return typed_id;
    }

    public void setTyped_id(Long typed_id) {
        this.typed_id = typed_id;
    }

    public Long getRes_id() {
        return res_id;
    }

    public void setRes_id(Long res_id) {
        this.res_id = res_id;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public int getUnitary() {
        return unitary;
    }

    public void setUnitary(int unitary) {
        this.unitary = unitary;
    }

    public LocalTime getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalTime starttime) {
        this.starttime = starttime;
    }
}
