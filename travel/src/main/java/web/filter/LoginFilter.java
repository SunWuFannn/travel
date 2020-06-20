package web.filter;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//自动登录判断的过滤器
//@WebFilter("/login.html")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //将父接口转为子接口
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //获取cookies
        Cookie[] cookies = request.getCookies();
        //判断是否自动登录，自动登录就转发
        if (cookies!=null&&cookies.length!=0){
            for (Cookie cookie : cookies) {
                if ("sessionIn".equals(cookie.getName()) && cookie.getValue() != null && "1".equals(cookie.getValue())) {
                    response.sendRedirect(request.getContextPath() + "/index.html");
                    filterChain.doFilter(request, response);
                }
            }
        }
        //否则就放行
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
