package service;

import domain.Category;

import java.util.List;

public interface CategoryService {
    //查询所有分类
    public List<Category> findAll();
}
