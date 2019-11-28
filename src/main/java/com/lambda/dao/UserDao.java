package com.lambda.dao;

import com.lambda.model.User;

public interface UserDao {
    User getUserByUsername(String username);
}
