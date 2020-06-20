package service;

import domain.PageBean;
import domain.Route;


public interface RouteService {

    //分页查询
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname);
    //查询商品详细信息
    public Route findOne(String rid);
}
