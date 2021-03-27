package com.lambda.dao.impl;

import com.lambda.constant.Gender;
import com.lambda.dao.UserProfileDao;
import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.dto.UserProfileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class UserProfileDaoImpl implements UserProfileDao {

    private final JdbcOperations jdbcOperations;

    private final SqlResultExtractor<UserProfileDTO> userProfileExtractor;

    @Autowired
    public UserProfileDaoImpl(JdbcOperations jdbcOperations,
                              SqlResultExtractor<UserProfileDTO> mapper) {
        this.jdbcOperations = jdbcOperations;
        this.userProfileExtractor = mapper;
    }

    @Override
    public Optional<UserProfileDTO> findProfileByUsername(String username) {
        String sql = "SELECT * FROM user_profile WHERE username = ?";
        return this.jdbcOperations.query(sql, this.userProfileExtractor.singleExtractor(), username);
    }

    @Override
    @Transactional
    public void createProfile(UserProfileDTO userProfileDTO) {
        String username = userProfileDTO.getUsername();
        String firstName = userProfileDTO.getFirstName();
        String lastName = userProfileDTO.getLastName();
        Timestamp dateOfBirth = userProfileDTO.getDateOfBirth();
        Gender gender = userProfileDTO.getGender();
        String phoneNumber = userProfileDTO.getPhoneNumber();
        String email = userProfileDTO.getEmail();
        String otherInfo = userProfileDTO.getOtherInfo();
        String sql = "INSERT INTO user_profile(username, first_name, last_name, date_of_birth, " +
                "gender, phone_number, email, other_info) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        this.jdbcOperations.update(sql, username, firstName, lastName, dateOfBirth,
                gender.getValue(), phoneNumber, email, otherInfo);
    }

    @Override
    @Transactional
    public void updateProfile(UserProfileDTO userProfileDTO) {
        String firstName = userProfileDTO.getFirstName();
        String lastName = userProfileDTO.getLastName();
        Timestamp dateOfBirth = userProfileDTO.getDateOfBirth();
        Gender gender = (userProfileDTO.getGender() == null) ? Gender.UNKNOWN : userProfileDTO.getGender();
        String phoneNumber = userProfileDTO.getPhoneNumber();
        String email = userProfileDTO.getEmail();
        String avatarUrl = userProfileDTO.getAvatarUrl();
        String otherInfo = userProfileDTO.getOtherInfo();
        String sql = "UPDATE user_profile SET first_name=?, " +
                "last_name=?, date_of_birth=?, gender=?, phone_number=?," +
                " email=?, avatar_url=?, other_info=? WHERE username=?";
        this.jdbcOperations.update(sql, firstName, lastName, dateOfBirth, gender.getValue(),
                phoneNumber, email, avatarUrl, otherInfo, userProfileDTO.getUsername());
    }
}
