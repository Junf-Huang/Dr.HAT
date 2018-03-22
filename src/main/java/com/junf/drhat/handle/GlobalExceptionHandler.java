package com.junf.drhat.handle;

import com.junf.drhat.bean.Result;
import com.junf.drhat.exception.MyException;
import com.junf.drhat.storage.StorageException;
import com.junf.drhat.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.PersistenceException;

//全局异常处理
@ControllerAdvice
public class GlobalExceptionHandler {
    private final static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof MyException) {
            MyException myException = (MyException) e;
            return ResultUtil.error(myException.getCode(), myException.getMessage());
        }


        if(e instanceof StorageException) {
            return ResultUtil.error(-2,e.getMessage());
        }

        logger.error("【系统异常】{}", e);
        return ResultUtil.error(-1, "未知错误");

    }

}
