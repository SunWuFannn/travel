package service;

import domain.Favorite;
import domain.Route;

import java.util.List;

public interface FavoriteService {
    //判断是否收藏
    public boolean isFavorite(int rid, int uid);
    //添加收藏
    public void add(String rid, int uid);
    //我的收藏
    public List<Route> find(int uid);
}
