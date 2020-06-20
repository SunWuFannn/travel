package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.ResultInfo;
import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import service.Impl.UserServiceImpl;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registUserServelt")
public class RegistUserServelt extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResultInfo resultInfo = new ResultInfo();
        ObjectMapper objectMapper = new ObjectMapper();

        //校验验证码
        String check = req.getParameter("check");
        String checkcode = (String) req.getSession().getAttribute("CHECKCODE_SERVER");
        //保证验证码只能使用一次
        req.getSession().removeAttribute("CHECKCODE_SERVER");
        if (checkcode == null || !checkcode.equalsIgnoreCase(check)) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败！验证码错误");
            resp.setContentType("application/json;charset=utf-8");
            objectMapper.writeValue(resp.getWriter(), resultInfo);
            return;
        }

        //获取数据
        Map<String, String[]> map = req.getParameterMap();
        //封装数据
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //调用service完成注册
        UserService userService = new UserServiceImpl();
        Boolean flag = userService.registUser(user);
        //响应结果
        if (flag) {
            resultInfo.setFlag(true);
        } else {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("注册失败！用户名重复");
        }
        resp.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(resp.getWriter(), resultInfo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
