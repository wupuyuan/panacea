package com.zhuooo.service;

public interface LoginService {

    /**
     * 登录
     *
     * @return token
     */
    String login(String userName, String password);

    /**
     * 返回用户信息
     */
    String queryUser(String token);
}
