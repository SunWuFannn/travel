package dao.Impl;

import dao.FavoriteDao;
import domain.Favorite;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.Date;
import java.util.List;

public class FavoriteDaoImpl implements FavoriteDao {
    JdbcTemplate jt = new JdbcTemplate(JDBCUtils.getDataSource());

    //根据rid和uid查询
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        String sql = "select * from tab_favorite where rid=? and uid=?";
        try {
            return jt.queryForObject(sql, new BeanPropertyRowMapper<>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            return null;
        }
    }

    //查询收藏次数
    @Override
    public int findCountByRid(int rid) {
        String sql = "select count(*) from tab_favorite where rid=?";
        return jt.queryForObject(sql, Integer.class, rid);
    }

    //添加收藏
    @Override
    public void add(int rid, int uid) {
        String sql = "insert into tab_favorite values(?,?,?)";
        jt.update(sql, rid, new Date(), uid);
    }

    @Override
    public List<Integer> findByUid(int uid) {
        String sql = "SELECT RID FROM tab_favorite WHERE UID=?";
        return jt.queryForList(sql, Integer.class, uid);
    }
}
