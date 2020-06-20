package service.Impl;

import dao.FavoriteDao;
import dao.Impl.FavoriteDaoImpl;
import dao.Impl.RouteDaoImpl;
import dao.RouteDao;
import domain.Favorite;
import domain.Route;
import service.FavoriteService;

import java.util.ArrayList;
import java.util.List;


public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();
    private RouteDao routeDao = new RouteDaoImpl();

    //判断是否收藏
    @Override
    public boolean isFavorite(int rid, int uid) {
        Favorite favorite = favoriteDao.findByRidAndUid(rid, uid);
        return favorite != null;
    }

    //添加收藏
    @Override
    public void add(String rid, int uid) {
        favoriteDao.add(Integer.parseInt(rid), uid);
    }

    //我的收藏
    @Override
    public List<Route> find(int uid) {
        //通过用户找到收藏路线的rid们
        List<Integer> rids = favoriteDao.findByUid(uid);
        if (rids == null || rids.size() == 0) {
            return null;
        }else {
            List<Route> routes = new ArrayList<>();
            //通过rid找到对应的路线
            for (Integer rid : rids) {
                Route one = routeDao.findOne(rid);
                routes.add(one);
            }
            return routes;
        }
    }
}
