package dao.Impl;

import dao.SellerDao;
import domain.Seller;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

public class SellerDaoImpl implements SellerDao {
    private JdbcTemplate jt = new JdbcTemplate(JDBCUtils.getDataSource());

    //查询商家信息
    @Override
    public Seller findBySid(int sid) {
        String sql = "select * from tab_seller where sid=?";
        return jt.queryForObject(sql, new BeanPropertyRowMapper<>(Seller.class), sid);
    }
}
