package service.Impl;

import dao.CategoryDao;
import dao.Impl.CategoryDaoImpl;
import domain.Category;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import service.CategoryService;
import util.JedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    //查询所有分类
    @Override
    public List<Category> findAll() {
        //从redis中查询
        Jedis jedis = JedisUtil.getJedis();
        //可以使用sortedset排序查询
        //Set<String> categories = jedis.zrange("category", 0, -1);
        //将id也查出来
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);
        List<Category> categoryList = null;
        //判断查询集合是否为空
        if (categories == null || categories.size() == 0) {
            //为空则是第一次访问，需要从数据库查询，再将数据存入redis
            categoryList = categoryDao.findAll();
            //将数据存储到redis中
            for (Category category : categoryList) {
                jedis.zadd("category", category.getCid(), category.getCname());
            }
        } else {
            //如果redis中有则转换为list返回
            categoryList = new ArrayList<Category>();
            for (Tuple tuple : categories) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                categoryList.add(category);
            }
        }

        return categoryList;
    }
}
