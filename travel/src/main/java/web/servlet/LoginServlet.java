package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.ResultInfo;
import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import service.Impl.UserServiceImpl;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResultInfo resultInfo = new ResultInfo();
        UserService userService = new UserServiceImpl();

        String check = req.getParameter("check");
        String checkcode = (String) req.getSession().getAttribute("CHECKCODE_SERVER");
        //保证验证码只能使用一次
        req.getSession().removeAttribute("CHECKCODE_SERVER");
        if (checkcode == null || !checkcode.equalsIgnoreCase(check)) {
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("登录失败！验证码错误");
            resp.setContentType("application/json;charset=utf-8");
            objectMapper.writeValue(resp.getWriter(), resultInfo);
            return;
        }

        //获取数据 封装对象
        Map<String, String[]> map = req.getParameterMap();
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //登录验证
        User loginUser = userService.login(user);
        if (loginUser == null) {
            //用户名或密码错误
            resultInfo.setFlag(false);
            resultInfo.setErrorMsg("用户名或密码错误！");
        } else {
            //用户名密码正确但是未激活
            if ("N".equals(loginUser.getStatus())) {
                resultInfo.setFlag(false);
                resultInfo.setErrorMsg("请先激活再登录！");
            } else {
            //用户名密码正确且激活了
                resultInfo.setFlag(true);
                //获取session
                HttpSession sessionUser = req.getSession();
                //正常登录且把用户信息写入user
                sessionUser.setAttribute("user", loginUser);
                //查询是否需要自动登录
                String autoLogin = req.getParameter("autoLogin");
                if ("on".equals(autoLogin)) {
                    //如果需要自动登录，写入cookie
                    Cookie cookieS = new Cookie("sessionIn", "1");
                    Cookie cookie = new Cookie("JSESSIONID", sessionUser.getId());
                    cookie.setMaxAge(60 * 60 * 24 * 7);
                    cookieS.setMaxAge(60 * 60 * 24 * 7);
                    resp.addCookie(cookie);
                    resp.addCookie(cookieS);
                }
            }
        }
        resp.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(resp.getWriter(), resultInfo);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }
}
