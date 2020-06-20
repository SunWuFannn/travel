package service.Impl;

import dao.FavoriteDao;
import dao.Impl.FavoriteDaoImpl;
import dao.Impl.RouteDaoImpl;
import dao.Impl.RouteImgDaoImpl;
import dao.Impl.SellerDaoImpl;
import dao.RouteDao;
import dao.RouteImgDao;
import dao.SellerDao;
import domain.PageBean;
import domain.Route;
import domain.RouteImg;
import domain.Seller;
import service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao = new RouteImgDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    //分页查询
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {

        //封装PageBean
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage); //设置当前页
        pb.setPageSize(pageSize);   //设置每页显示数
        pb.setTotalCount(routeDao.findTotalCount(cid, rname));    //设置记录数
        pb.setList(routeDao.findByPage(cid, (currentPage - 1) * pageSize, pageSize, rname)); //设置当前页显示数据集合
        pb.setTotalPage(pb.getTotalCount() % pageSize == 0 ? pb.getTotalCount() / pageSize : (pb.getTotalCount() / pageSize) + 1);  //设置总页数
        return pb;
    }

    //查询商品详细信息
    @Override
    public Route findOne(String rid) {
        //根据rid查询route表
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //根据rid查询图片集合信息
        List<RouteImg> imgList = routeImgDao.findByRid(Integer.parseInt(rid));
        route.setRouteImgList(imgList);
        //根据sid查询商家信息
        Seller seller = sellerDao.findBySid(route.getSid());
        route.setSeller(seller);
        //查询收藏次数
        int count = favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);
        return route;
    }
}
