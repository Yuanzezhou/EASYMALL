package com.harbin.mymall.mymallproduct.exception;

import com.harbin.common.exception.BizCodeEnum;
import com.harbin.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yuanzz
 * @creat 2021-01-29-16:10
 */

@ControllerAdvice(basePackages = "com.harbin.mymall.mymallproduct.controller")
@ResponseBody
@Slf4j
public class MymallExceptionControllerAdvice {
    @ExceptionHandler(value= MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        log.error("数据校验问题{}，异常是:{}",e.getMessage(),e.getClass());
        BindingResult bindingResult = e.getBindingResult();
        Map<String,String> map = new HashMap<>();
        bindingResult.getFieldErrors().forEach((fieldError)-> {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(),BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data",map);
    }

    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable throwable){
        return R.error(BizCodeEnum.UNKNOW_EXEPTION.getCode(),BizCodeEnum.UNKNOW_EXEPTION.getMsg());
    }
}
