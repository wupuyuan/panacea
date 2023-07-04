package com.zhuooo.demo;

import org.springframework.util.StringUtils;

import java.io.Serializable;

public class ZhuoooResponse<T> implements Serializable {

    private int code;

    private String info;

    private String text;

    protected T data;


    public static <T> ZhuoooResponse<T> success(T data) {
        ZhuoooResponse ret = new ZhuoooResponse();
        ret.code = 0;
        ret.info = "success";
        ret.data = data;
        return ret;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
