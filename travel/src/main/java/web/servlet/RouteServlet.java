package web.servlet;

import domain.PageBean;
import domain.Route;
import domain.User;
import service.FavoriteService;
import service.Impl.FavoriteServiceImpl;
import service.Impl.RouteServiceImpl;
import service.RouteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    //分页查询
    public void pageQuery(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收参数
        String currentPageStr = req.getParameter("currentPage");
        String pageSizeStr = req.getParameter("pageSize");
        String cidStr = req.getParameter("cid");

        //接收rname，线路名称
        String rname = req.getParameter("rname");
        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");

        //处理参数
        int cid = 0;    //类别id
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        int currentPage = 0;    //当前页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        int pageSize = 0;   //每页显示条数
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 10;
        }

        //调用service查询
        PageBean<Route> pb = routeService.pageQuery(cid, currentPage, pageSize, rname);
        //将PageBean对象序列化为json返回
        writeValue(pb, resp);
    }

    //查询商品详细信息
    public void findOne(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //接收参数rid
        String rid = req.getParameter("rid");
        //调用service
        Route route = routeService.findOne(rid);
        //转为json写回客户端
        writeValue(route, resp);
    }

    //判断是否收藏
    public void isFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取rid和uid
        String rid = req.getParameter("rid");
        User user = (User) req.getSession().getAttribute("user");
        int uid = 0;
        if (user != null) {
            uid = user.getUid();
        }
        //调用favoriteservice
        boolean flag = favoriteService.isFavorite(Integer.parseInt(rid), uid);
        writeValue(flag, resp);
    }

    //添加收藏
    public void addFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取rid
        String rid = req.getParameter("rid");
        //获取当前登录用户
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            return;
        }
        int uid = user.getUid();
        //调用service
        favoriteService.add(rid, uid);
    }

    //我的收藏
    public void myFavorite(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取当前登录用户
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            writeValue(null, resp);
            return;
        }
        int uid = user.getUid();
        //调用service
        List<Route> routes = favoriteService.find(uid);
        writeValue(routes, resp);
    }
}
