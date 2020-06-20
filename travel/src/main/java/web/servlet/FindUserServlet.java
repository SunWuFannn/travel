package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/findUserServlet")
public class FindUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中获取登录用户
        User user = (User) req.getSession().getAttribute("user");
        //将user写回
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(resp.getOutputStream(), user);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
