package web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exitServlet")
public class ExitServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //销毁cookie
        Cookie cookie = new Cookie("JSESSIONID", "");
        Cookie cookieS = new Cookie("sessionIn", "0");
        cookie.setMaxAge(0);
        cookieS.setMaxAge(0);
        resp.addCookie(cookie);
        resp.addCookie(cookieS);
        //销毁session
        req.getSession().invalidate();
        //跳转页面
        resp.sendRedirect(req.getContextPath()+"/login.html");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
