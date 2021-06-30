package com.harbin.mymall.mymallseckill.config;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.adapter.servlet.callback.WebCallbackManager;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.harbin.common.exception.BizCodeEnum;
import com.harbin.common.utils.R;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;

/**
 * @author Yuanzz
 * @creat 2021-03-14-16:07
 */

@Configuration
public class SeckillSentinelConfig {

    public SeckillSentinelConfig(){
        WebCallbackManager.setUrlBlockHandler(new UrlBlockHandler() {
            @Override
            public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
                R error = R.error(BizCodeEnum.TOO_MANY_REQUEST.getCode(), BizCodeEnum.TOO_MANY_REQUEST.getMsg());
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json");
                httpServletResponse.getWriter().write(JSON.toJSONString(error));
            }
        });
    }
}
