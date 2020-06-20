package dao.Impl;

import dao.RouteImgDao;
import domain.RouteImg;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.List;

public class RouteImgDaoImpl implements RouteImgDao {
    private JdbcTemplate jt = new JdbcTemplate(JDBCUtils.getDataSource());

    //查询商品对应的图片集合
    @Override
    public List<RouteImg> findByRid(int rid) {
        String sql = "select * from tab_route_img where rid=?";
        return jt.query(sql, new BeanPropertyRowMapper<>(RouteImg.class), rid);
    }
}
