package dao;

import domain.RouteImg;

import java.util.List;

public interface RouteImgDao {
    //查询商品对应的图片集合
    public List<RouteImg> findByRid(int rid);
}
