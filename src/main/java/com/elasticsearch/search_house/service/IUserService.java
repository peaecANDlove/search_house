package com.elasticsearch.search_house.service;

import com.elasticsearch.search_house.model.User;

/**
 * 用户服务
 */
public interface IUserService {

    User findUserByName(String userName);
}
