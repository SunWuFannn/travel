package web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import domain.ResultInfo;
import domain.User;
import org.apache.commons.beanutils.BeanUtils;
import service.Impl.UserServiceImpl;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*") // /user/add /user/find
public class UserServlet extends BaseServlet {

    //声明UserService业务对象
    private UserService userService = new UserServiceImpl();

    /**
     * 注册功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

    /**
     * 登录功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ResultInfo resultInfo = new ResultInfo();

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

        //自动登录的判断,如果是自动登录的就直接设置flag为true，然后重定向
        Cookie[] cookies = req.getCookies();
        for (Cookie cookie : cookies) {
            if ("JSESSIONID".equals(cookie.getName()) && cookie.getValue() != null) {
                resultInfo.setFlag(true);
                resp.setContentType("application/json;charset=utf-8");
                objectMapper.writeValue(resp.getWriter(), resultInfo);
            }
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
                    Cookie cookie = new Cookie("JSESSIONID", sessionUser.getId());
                    cookie.setMaxAge(60 * 60 * 24 * 7);
                    resp.addCookie(cookie);
                }
            }
        }
        resp.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(resp.getWriter(), resultInfo);
    }

    /**
     * 查询单个对象
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //从session中获取登录用户
        User user = (User) req.getSession().getAttribute("user");
        //将user写回
        ObjectMapper objectMapper = new ObjectMapper();
        resp.setContentType("application/json;charset=utf-8");
        objectMapper.writeValue(resp.getOutputStream(), user);
    }

    /**
     * 退出功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //销毁cookie
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
        //销毁session
        req.getSession().invalidate();
        //跳转页面
        resp.sendRedirect(req.getContextPath()+"/login.html");
    }

    /**
     * 激活功能
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取激活码
        String code = req.getParameter("code");
        if (code == null) {
            return;
        }
        //获取是否激活成功
        boolean flag = userService.active(code);
        String msg = "";
        if (flag) {
            msg = "激活成功！请<a href='login.html'>登录</a>";
        } else {
            msg = "激活失败！";
        }
        resp.getWriter().write(msg);
    }
}
