package com.harbin.mymallcart.interceptor;

/**
 * @author Yuanzz
 * @creat 2021-03-07-16:20
 */

import com.harbin.common.constant.AuthServerConstant;
import com.harbin.common.vo.MemberRespVo;
import com.harbin.mymallcart.constant.CartConstant;
import com.harbin.mymallcart.vo.UserInfoTo;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @Description: 在执行目标方法之前，判断用户的登录状态。并封装传递给目标请求
 * @Author: WangTianShun
 * @Date: 2020/11/21 14:56
 * @Version 1.0
 */
public class CartInterceptor implements HandlerInterceptor {
    //ThreadLocal同一个线程共享数据
    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();
    /**
     * 在目标方法执行之前拦截
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserInfoTo userInfoTo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberRespVo member = (MemberRespVo) session.getAttribute(AuthServerConstant.LOGIN_USER);
        if (member != null){
            //用户登录
            userInfoTo.setUserId(member.getId());
        }
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length >0){
            for (Cookie cookie : cookies) {
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_COOKIE_NAME)){
                    userInfoTo.setUserKey(cookie.getValue());
                    userInfoTo.setTempUser(true);
                }
            }
        }

        //如果没有临时用户，一定保存一个临时用户
        if (StringUtils.isEmpty(userInfoTo.getUserKey())){
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }
        //目标方法执行之前
        threadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 业务执行之后 分配临时用户，让浏览器保存
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserInfoTo userInfoTo = threadLocal.get();
        //如果没有临时用户，第一次访问购物车就添加临时用户
        if (!userInfoTo.isTempUser()){
            //持续的延长用户的过期时间
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            cookie.setDomain("easymall.com");
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }
}
