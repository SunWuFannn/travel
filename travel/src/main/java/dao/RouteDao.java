package dao;

import domain.Route;

import java.util.List;

public interface RouteDao {

    //根据cid和rname查询总记录数
    public int findTotalCount(int cid, String rname);
    //根据cid，start,pageSize，rname查询当前页显示的集合
    public List<Route> findByPage(int cid, int start, int pageSize, String rname);
    //根据rid查询
    public Route findOne(int rid);
}
