package hndt.radiolibs.bean;


import hndt.radiolibs.util.GSON;

import java.io.Serializable;

/**
 * 一. 3种信息提示信息,出现在窗口顶部,可自动关闭.用code表示-1错误\0警告\1提示,
 * 二. 用div模拟模态窗口, 1.确认离开此页面吗?(离开,保留)  2.你的账号已经在其他地方登陆,需要重新登陆吗?(确定,取消)  3.会话超时,请重新登陆.(确定)
 */
public class TipBean implements Serializable {
    int code = -9; //-1错误 0信息
    String text;  //提示信息
    String target; //哪一个组件的问题

    public TipBean() {
    }

    public TipBean(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public void fill(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public void fill(int code, String target, String text) {
        this.code = code;
        this.target = target;
        this.text = text;
    }

    public void fill(int code, String target, String text, boolean autoClose) {
        this.code = code;
        this.target = target;
        this.text = text;
    }

    public void clear(){
        fill(-9,null,null);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }


}
