package com.lambda.dao;

import com.lambda.model.CustomUser;

public interface UserDao {

    CustomUser getUserByUsername(String username);
}
