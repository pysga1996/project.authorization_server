package com.lambda.dao.impl;

import com.lambda.constant.Gender;
import com.lambda.dao.UserProfileDao;
import com.lambda.model.dto.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class UserProfileDaoImpl implements UserProfileDao {

    private final JdbcOperations jdbcOperations;

    private final RowMapper<UserProfileDTO> mapper;

    @Autowired
    public UserProfileDaoImpl(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
        this.mapper = new BeanPropertyRowMapper<>();
    }

    @Override
    public UserProfileDTO findProfileById(Long id) {
        String sql = "SELECT * FROM user_profile WHERE id=:id";
        return this.jdbcOperations.queryForObject(sql, this.mapper, id);
    }

    @Override
    public void createProfile(UserProfileDTO userProfileDTO) {
        Long userId = userProfileDTO.getUserId();
        String firstName = userProfileDTO.getFirstName();
        String lastName = userProfileDTO.getLastName();
        Timestamp dateOfBirth = userProfileDTO.getDateOfBirth();
        Gender gender = userProfileDTO.getGender();
        String phoneNumber = userProfileDTO.getPhoneNumber();
        String email = userProfileDTO.getEmail();
        String sql = "INSERT INTO user_profile(user_id, first_name, last_name, date_of_birth, " +
                "gender, phone_number, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        this.jdbcOperations.update(sql, userId, firstName, lastName, dateOfBirth,
                gender.getValue(), phoneNumber, email);
    }

    @Override
    public void updateProfile(UserProfileDTO userProfileDTO) {
        String firstName = userProfileDTO.getFirstName();
        String lastName = userProfileDTO.getLastName();
        Timestamp dateOfBirth = userProfileDTO.getDateOfBirth();
        Gender gender = userProfileDTO.getGender();
        String phoneNumber = userProfileDTO.getPhoneNumber();
        String email = userProfileDTO.getEmail();
        String avatarUrl = userProfileDTO.getAvatarUrl();
        String sql = "UPDATE user_profile SET first_name=?, " +
                "last_name=?, date_of_birth=?, gender=?, phone_number=?," +
                " email=?, avatar_url=? WHERE id=?";
        this.jdbcOperations.update(sql, firstName, lastName, dateOfBirth, gender.getValue(),
                phoneNumber, email, avatarUrl, userProfileDTO.getId());
    }
}
