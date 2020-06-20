package service.Impl;

import dao.Impl.UserDaoImpl;
import dao.UserDao;
import domain.User;
import service.UserService;
import util.MailUtils;
import util.UuidUtil;


public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    //注册用户
    @Override
    public Boolean registUser(User user) {
        //如果用户名已经存在，那么返回false，不然返回true
        if (userDao.findUserByName(user.getUsername()) != null) {
            return false;
        } else {
            //设置激活码，唯一字符串
            user.setCode(UuidUtil.getUuid());
            //设置激活状态
            user.setStatus("N");
            //如果用户名不存在 保存信息
            userDao.save(user);
            //发送邮件
            String content = "<a href='http://localhost:8080/travel/user/active?code=" + user.getCode() + "'>点击激活邮箱</a>";
            MailUtils.sendMail(user.getEmail(), content, "激活邮件");
            return true;
        }
    }

    //激活邮箱
    @Override
    public boolean active(String code) {
        //寻找是否有该用户
        User user = userDao.findByCode(code);
        if (user == null) {
            return false;
        } else {
            //有该用户就修改激活状态
            userDao.updateStatus(user);
            return true;
        }
    }

    //登录
    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
