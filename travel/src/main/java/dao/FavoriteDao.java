package dao;

import domain.Favorite;

import java.util.List;

public interface FavoriteDao {
    //根据rid和uid查询
    public Favorite findByRidAndUid(int rid, int uid);
    //查询收藏次数
    public int findCountByRid(int rid);
    //添加收藏
    public void add(int rid, int uid);
    //通过uid找rid
    public List<Integer> findByUid(int uid);
}
