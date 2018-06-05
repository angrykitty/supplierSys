package com.supplier.service;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.supplier.common.model.User;

public class UserService {
    public static final UserService me = new UserService();

    private User userDao = new User().dao();

    //用户名密码查找用户
    public User getByUserNameAndPwd(String userName, String pwd) throws Exception {

        User user = userDao.findFirst("SELECT * FROM user WHERE userName = ? AND pwd = ?", userName, pwd);

        return user;
    }

    public User createUser(User user) throws Exception {
        if (user == null) {
            throw new Exception("user is required");
        }
        if (StringUtils.isEmpty(user.getName())) {
            throw new Exception("user.name is required");
        }
        if (StringUtils.isEmpty(user.getPwd())) {
            throw new Exception("user.pwd is required");
        }
        if (StringUtils.isEmpty(user.getUserName())) {
            throw new Exception("user.userName is required");
        }
        if (StringUtils.isEmpty(user.getSupplierId())) {
            throw new Exception("user.supplierId is required");
        }
        if (StringUtils.isEmpty(user.getName())) {
            throw new Exception("user is required");
        }
        if (StringUtils.isEmpty(user.getName())) {
            throw new Exception("user is required");
        }
        return null;
    }

    /**
     * 添加用户信息
     *
     * @return
     */
    public boolean addUserName(User user) {
        return Db.save("user", user.toRecord());
    }

    /**
     * 根据用户名称查询用户信息
     *
     * @param userName
     * @return
     */
    public User findUserByUserName(String userName) {
        User user = userDao.findFirst("select id,userName,name,pwd,crearteDate,role,tel,supplierId from user where userName=?", userName);
        return user;
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    public boolean modifyUser(User user) {
        return Db.update("user", user.toRecord());
    }

    /**
     * 获取用户信息
     * @param pageNum
     * @param pageSize
     * @param name 名称
     * @param supplierId 编号
     * @param startTime 创建开始时间
     * @param endTime  创建结束时间
     * @return
     */
    public Page<User> findPageUser(Integer pageNum, Integer pageSize,
                            String name, String supplierId, String startTime, String endTime) {
        String totalRowSql = "select count(*) from user where 1=1";
        String findSql = "select * from user where 1=1";
        if (!StringUtils.isEmpty(name)) {
            totalRowSql += " and  name like  '%" + name + "%'";
            findSql += " and  name like  '%" + name + "%'";
        }

        if (!StringUtils.isEmpty(supplierId)){
            totalRowSql += " and supplierId = " + supplierId;
            findSql += " and supplierId = " + supplierId;
        }

        if (!StringUtils.isEmpty(startTime)){
            totalRowSql += " and crearteDate > str_to_date('" + startTime + "', '%Y-%m-%d')";
            findSql += " and crearteDate > str_to_date('" + startTime + "', '%Y-%m-%d')";
        }
        if (!StringUtils.isEmpty(endTime)){
            totalRowSql += " and crearteDate <=" + "date_sub(str_to_date('" + endTime + "', '%Y-%m-%d'),interval -1 day)";
            findSql += " and crearteDate <=" + "date_sub(str_to_date('" + endTime + "', '%Y-%m-%d'),interval -1 day)";
        }
        findSql += " ORDER BY crearteDate DESC";
        return userDao.paginateByFullSql(pageNum, pageSize, totalRowSql, findSql);
    }
}