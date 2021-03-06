package com.map.web.interceptor;

import com.auth0.jwt.interfaces.Claim;
import com.map.utils.JWTUtils;
import com.map.utils.JedisUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


public class TokenInterceptor implements HandlerInterceptor {
    private Logger logger = Logger.getLogger(TokenInterceptor.class);
    // 设置不拦截的路径
    private static final String[] IGNORE_URL = {"/user", "Form", "/utils", "/none", "/photo",
                                            "/audio", "/video", "/register", "/admin" ,"/qqq"};
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        boolean flag = false;
        String path = request.getServletPath();
        for (String s : IGNORE_URL) {
            if (path.contains(s)) {
                flag = true;
                break;
            }
        }
        if (!flag) {
            String token = request.getHeader("token");
            if (token == null) {
                // 跳转返回未登录
                request.getRequestDispatcher("/utils/notLoginIn").forward(request, response);
                logger.info("未登录");
            } else {
                int id = 0;
                try {
                    Map<String, Claim> map = JWTUtils.verifyToken(token);
                    id = map.get("id").asInt();
                    request.setAttribute("id", id);
                    System.out.println(id);
                } catch (Exception e) {
                    request.getRequestDispatcher("/utils/logonExpires").forward(request, response);
                    logger.info("登录过期");
                }
                // 在Redis中查询存储的token
                String sToken = JedisUtils.getToken(String.valueOf(id));
                if (!token.equals(sToken)) {
                    // 登录异常，需要强制下线
                    logger.info("登录异常");
                    request.getRequestDispatcher("/utils/loginException").forward(request, response);
                } else {
                    flag = true;
                }
            }
        }
        return flag;
    }
}
