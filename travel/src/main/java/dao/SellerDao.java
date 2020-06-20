package dao;

import domain.Seller;

public interface SellerDao {
    //查询商家信息
    public Seller findBySid(int sid);
}
