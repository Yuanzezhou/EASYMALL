package com.harbin.mymall.mymallorder.interceptor;

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
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean match = antPathMatcher.match("/mymallorder/order/status/**", uri);
        boolean match1 = antPathMatcher.match("/payed/notify", uri);

        if (match || match1){
            return true;
        }

        MemberRespVo attribute = (MemberRespVo) request.getSession().getAttribute(AuthServerConstant.LOGIN_USER);
        if (attribute != null){
            //已经登录，用户信息保存在
            loginUser.set(attribute);
            //继续执行业务
            return true;
        }else {
            //没登录就去登录
            request.getSession().setAttribute("msg","请先进行登录");
            response.sendRedirect("http://auth.easymall.com/login.html");
            //直接返回，不继续执行后续业务
            return false;
        }
    }
}