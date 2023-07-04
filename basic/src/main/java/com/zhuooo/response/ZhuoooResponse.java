package com.zhuooo.response;

import com.zhuooo.exception.ZhuoooException;
import com.zhuooo.constant.ReturnCode;
import org.springframework.util.StringUtils;

import java.io.Serializable;

public class ZhuoooResponse<T> implements Serializable {

    private int code;

    private String info;

    private String text;

    protected T data;

    public static ZhuoooResponse error(ZhuoooException exception) {
        ZhuoooResponse ret = new ZhuoooResponse();
        ret.code = exception.getCode();
        ret.info = exception.getInfo();
        StringBuilder sb = new StringBuilder("return error");
        if (!StringUtils.isEmpty(ret.getText())) {
            sb.append(": ").append(ret.getText());
        }
        sb.append(", ").append("please check log by id [").append(exception.getUuid()).append("]");
        ret.setText(sb.toString());
        return ret;
    }

    public static <T> ZhuoooResponse<T> success(T data) {
        ZhuoooResponse ret = new ZhuoooResponse();
        ret.code = ReturnCode.SUCCESS.getCode();
        ret.info = ReturnCode.SUCCESS.getInfo();
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
