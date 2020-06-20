package dao.Impl;

import dao.RouteDao;
import domain.Route;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate jt = new JdbcTemplate(JDBCUtils.getDataSource());

    //根据cid和rname查询总记录数
    @Override
    public int findTotalCount(int cid, String rname) {
        //String sql = "select count(*) from tab_route where cid=?";
        //定义模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        //条件集合
        List params = new ArrayList<>();
        //判断参数是否有值
        if (cid != 0) {
            sb.append(" and cid=? ");
            params.add(cid);
        }
        if (rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        sql = sb.toString();
        return jt.queryForObject(sql, Integer.class, params.toArray());
    }

    //根据cid，start,pageSize，rname查询当前页显示的集合
    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        String sql = "select * from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);
        //条件集合
        List params = new ArrayList<>();
        //判断参数是否有值
        if (cid != 0) {
            sb.append(" and cid=? ");
            params.add(cid);
        }
        if (rname != null && rname.length() > 0) {
            sb.append(" and rname like ? ");
            params.add("%" + rname + "%");
        }
        //分页条件
        sb.append(" limit ? , ? ");
        params.add(start);
        params.add(pageSize);
        sql = sb.toString();
        return jt.query(sql, new BeanPropertyRowMapper<>(Route.class), params.toArray());
    }

    //根据rid查询
    @Override
    public Route findOne(int rid) {
        String sql = "select * from tab_route where rid=?";
        return jt.queryForObject(sql, new BeanPropertyRowMapper<>(Route.class), rid);
    }
}
