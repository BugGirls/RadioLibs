package hndt.radiolibs.bean;

import java.io.Serializable;

/**
 * 对应数据库字段，resources.format_mark
 * <p>
 * 适用于声乐。
 * 定义:出入点时间段内的片段注释，包括Trim Start(前奏、开始有声处)、Intro 1(伴唱1、出 人声处)、Intro 2(伴唱2)、Intro 3(主歌、主唱)、Hook Start(副歌开始)、Hook End(副歌 结束)、Next to Play(淡出)、Trim End(结束)、earlyNext(人声结束点);
 */
public class ColumnMarkBean implements Serializable {
    Long trimStart;
    Long intro1;
    Long intro2;
    Long intro3;
    Long hookStart;
    Long hookEnd;
    Long nextToPlay;
    Long trimEnd;
    Long earlyNext;

    public Long getTrimStart() {
        return trimStart;
    }

    public void setTrimStart(Long trimStart) {
        this.trimStart = trimStart;
    }

    public Long getIntro1() {
        return intro1;
    }

    public void setIntro1(Long intro1) {
        this.intro1 = intro1;
    }

    public Long getIntro2() {
        return intro2;
    }

    public void setIntro2(Long intro2) {
        this.intro2 = intro2;
    }

    public Long getIntro3() {
        return intro3;
    }

    public void setIntro3(Long intro3) {
        this.intro3 = intro3;
    }

    public Long getHookStart() {
        return hookStart;
    }

    public void setHookStart(Long hookStart) {
        this.hookStart = hookStart;
    }

    public Long getHookEnd() {
        return hookEnd;
    }

    public void setHookEnd(Long hookEnd) {
        this.hookEnd = hookEnd;
    }

    public Long getNextToPlay() {
        return nextToPlay;
    }

    public void setNextToPlay(Long nextToPlay) {
        this.nextToPlay = nextToPlay;
    }

    public Long getTrimEnd() {
        return trimEnd;
    }

    public void setTrimEnd(Long trimEnd) {
        this.trimEnd = trimEnd;
    }

    public Long getEarlyNext() {
        return earlyNext;
    }

    public void setEarlyNext(Long earlyNext) {
        this.earlyNext = earlyNext;
    }
}
