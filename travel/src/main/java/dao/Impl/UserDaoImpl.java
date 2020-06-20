package dao.Impl;

import dao.UserDao;
import domain.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

public class UserDaoImpl implements UserDao {
    JdbcTemplate jt = new JdbcTemplate(JDBCUtils.getDataSource());

    //查询数据库用户名是否已经注册
    @Override
    public User findUserByName(String username) {
        String sql = "select * from tab_user where username=?";
        try {
            return jt.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //保存信息
    @Override
    public int save(User user) {
        String sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        return jt.update(sql, user.getUsername(), user.getPassword(), user.getName(),
                user.getBirthday(), user.getSex(), user.getTelephone(), user.getEmail(),
                user.getStatus(), user.getCode());
    }

    //更新激活后用户状态
    @Override
    public void updateStatus(User user) {
        String sql = "update tab_user set status = 'Y' where uid=?";
        jt.update(sql, user.getUid());
    }

    //通过激活码查询数据库
    @Override
    public User findByCode(String code) {
        String sql = "select * from tab_user where code=?";
        try {
            return jt.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), code);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    //登录查询
    @Override
    public User findByUsernameAndPassword(String username, String password) {
        String sql = "select * from tab_user where username=? and password=?";
        try {
            return jt.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username, password);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
