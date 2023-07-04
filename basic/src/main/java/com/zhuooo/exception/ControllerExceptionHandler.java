package com.zhuooo.exception;

import com.zhuooo.constant.ReturnCode;
import com.zhuooo.response.ZhuoooResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    //处理自定义的异常
    @ExceptionHandler(ZhuoooException.class)
    public ZhuoooResponse customHandler(ZhuoooException e) {
        e.printStackTrace();
        return ZhuoooResponse.error(e);
    }

    //其他未处理的异常
    @ExceptionHandler(Exception.class)
    public Object exceptionHandler(Exception e) {
        ZhuoooException ze = new ZhuoooException(ReturnCode.UNKNOWN, e);
        e.printStackTrace();
        return ZhuoooResponse.error(ze);
    }
}
