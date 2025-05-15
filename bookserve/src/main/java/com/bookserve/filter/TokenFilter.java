package com.bookserve.filter;

import com.bookserve.utils.CurrentHolder;
import com.bookserve.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@WebFilter(urlPatterns = "/*")
public class TokenFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //1. 获取到请求路径
        String requestURI = request.getRequestURI(); // /employee/login

        //2. 判断是否是登录请求, 如果路径中包含 /login, 说明是登录操作, 放行
        if (requestURI.contains("/login")){
            log.info("登录请求, 放行");
            filterChain.doFilter(request, response);
            return;
        }
        // 新增：判断是否是注册请求
        if (requestURI.contains("/register")) {
            log.info("注册请求, 放行");
            filterChain.doFilter(request, response);
            return;
        }
        if (requestURI.contains("/files/download")||requestURI.contains("/files/upload")||requestURI.contains("/files")){
            log.info("文件显示上传请求, 放行");
            filterChain.doFilter(request, response);
            return;
        }

        //3. 获取请求头中的token
        String token = request.getHeader("token");
        log.info("接收到的令牌: {}", token);

        //4. 判断token是否存在, 如果不存在, 说明用户没有登录, 返回错误信息(响应401状态码)
        if (token == null || token.isEmpty()){
            log.info("令牌为空, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }



        //5. 如果token存在, 校验令牌, 如果校验失败 -> 返回错误信息(响应401状态码)
        try {
            Claims claims = JwtUtils.parseToken(token);
            Integer empId = Integer.valueOf(claims.get("id").toString());
            CurrentHolder.setCurrentId(empId); //存入
            log.info("当前登录员工ID: {}, 将其存入ThreadLocal", empId);
        } catch (Exception e) {
            log.info("令牌非法, 响应401");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        //6. 校验通过, 放行
        log.info("令牌合法, 放行");
        filterChain.doFilter(request, response);

        //7. 删除ThreadLocal中的数据
        CurrentHolder.remove();
    }
}
