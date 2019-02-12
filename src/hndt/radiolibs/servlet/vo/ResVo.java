package hndt.radiolibs.servlet.vo;

import hndt.radiolibs.bean.ResBean;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;

public class ResVo {

    Long id;
    String name;
    String uuid;
    int category;
    int duration; //时长
    int playDuration; //播放时长
    Long starting_point; //入点，音频资料正式有效内容的起始点时间;
    String path; //存放路径
    Long size;   //文件大小
    Long clockId;
    Long typeId;
    Long runtimeId;
    int placeholder; //是否占位
    int unitary;//不可截断
    String playdate;
    LocalTime starttime;
    LocalTime endtime;
    Long starting;//入点，音频资料正式有效内容的起始点时间
    String singing;//演唱者
    String lyrics;//词
    String music;//曲

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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPlayDuration() {
        return playDuration;
    }

    public void setPlayDuration(int playDuration) {
        this.playDuration = playDuration;
    }

    public Long getStarting_point() {
        return starting_point;
    }

    public void setStarting_point(Long starting_point) {
        this.starting_point = starting_point;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getClockId() {
        return clockId;
    }

    public void setClockId(Long clockId) {
        this.clockId = clockId;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public Long getRuntimeId() {
        return runtimeId;
    }

    public void setRuntimeId(Long runtimeId) {
        this.runtimeId = runtimeId;
    }

    public String getPlaydate() {
        return playdate;
    }

    public void setPlaydate(String playdate) {
        this.playdate = playdate;
    }

    public LocalTime getStarttime() {
        return starttime;
    }

    public void setStarttime(LocalTime starttime) {
        this.starttime = starttime;
    }

    public LocalTime getEndtime() {
        return endtime;
    }

    public void setEndtime(LocalTime endtime) {
        this.endtime = endtime;
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

    public Long getStarting() {
        return starting;
    }

    public void setStarting(Long starting) {
        this.starting = starting;
    }

    public String getSinging() {
        return singing;
    }

    public void setSinging(String singing) {
        this.singing = singing;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

}
