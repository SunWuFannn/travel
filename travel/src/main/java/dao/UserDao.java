package dao;

import domain.User;

public interface UserDao {
    //查询数据库用户名是否已经注册
    User findUserByName(String username);

    //保存信息
    int save(User user);

    //更新激活后用户状态
    void updateStatus(User user);

    //通过激活码查询数据库
    User findByCode(String code);

    //通过用户名和密码查询数据库
    User findByUsernameAndPassword(String username, String password);
}
