package dao;

import domain.Category;

import java.util.List;

public interface CategoryDao {
    //查询所有分类
    public List<Category> findAll();
}
