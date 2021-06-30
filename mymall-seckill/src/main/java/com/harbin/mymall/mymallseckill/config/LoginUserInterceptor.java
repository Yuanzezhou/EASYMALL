package com.harbin.mymall.mymallseckill.config;

import com.harbin.common.constant.AuthServerConstant;
import com.harbin.common.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Yuanzz
 * @creat 2021-03-07-22:39
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo> loginUser = new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String uri = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/kill", uri);
        // 如果是秒杀，需要判断是否登录，其他路径直接放行不需要判断
        if (match) {
            MemberRespVo attribute = (MemberRespVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
            if (attribute != null){
                loginUser.set(attribute);
                return true;
            }else {
                //没登录就去登录
                request.getSession().setAttribute("msg","请先进行登录");
                response.sendRedirect("http://auth.easymall.com/login.html");
                return false;
            }
        }
        return true;
    }
}