package com.zhuooo.exception;

import com.zhuooo.constant.ReturnCode;
import com.zhuooo.utils.UuidUtils;

public class ZhuoooException extends RuntimeException {

    private String uuid;

    private int code;

    private String info;

    private String text;

    public ZhuoooException(ReturnCode returnCode) {
        this.uuid = UuidUtils.generateUuid();
        this.code = returnCode.getCode();
        this.info = returnCode.getInfo();
    }

    public ZhuoooException(ReturnCode returnCode, String text) {
        this.uuid = UuidUtils.generateUuid();
        this.code = returnCode.getCode();
        this.info = returnCode.getInfo();
        this.text = text;
    }

    public ZhuoooException(ReturnCode returnCode, Throwable cause) {
        super(cause);
        this.uuid = UuidUtils.generateUuid();
        this.code = returnCode.getCode();
        this.info = returnCode.getInfo();
    }

    public ZhuoooException(ReturnCode returnCode, String text, Throwable cause) {
        super(cause);
        this.uuid = UuidUtils.generateUuid();
        this.code = returnCode.getCode();
        this.info = returnCode.getInfo();
        this.text = text;
    }


    public String getUuid() {
        return uuid;
    }

    public int getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public String getText() {
        return text;
    }

}

