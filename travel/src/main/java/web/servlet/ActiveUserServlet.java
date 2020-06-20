package web.servlet;

import service.Impl.UserServiceImpl;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取激活码
        String code = req.getParameter("code");
        if (code == null) {
            return;
        }
        //获取是否激活成功
        UserService userService = new UserServiceImpl();
        boolean flag = userService.active(code);
        String msg = "";
        if (flag) {
            msg = "激活成功！请<a href='login.html'>登录</a>";
        } else {
            msg = "激活失败！";
        }
        resp.getWriter().write(msg);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
