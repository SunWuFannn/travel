package dao.Impl;

import dao.CategoryDao;
import domain.Category;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import util.JDBCUtils;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private JdbcTemplate jt = new JdbcTemplate(JDBCUtils.getDataSource());

    //查询所有分类
    @Override
    public List<Category> findAll() {
        String sql = "select * from tab_category";
        return jt.query(sql, new BeanPropertyRowMapper<>(Category.class));
    }
}
