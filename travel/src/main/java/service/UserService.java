package service;

import domain.User;

public interface UserService {

    //注册用户
    Boolean registUser(User user);
    //激活邮箱
    boolean active(String code);
    //登录
    User login(User user);
}
