package com.lambda.dao.extractor.impl;

import com.lambda.constant.Gender;
import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.dto.UserProfileDTO;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserProfileExtractor implements SqlResultExtractor<UserProfileDTO> {

    @Override
    public ResultSetExtractor<Optional<UserProfileDTO>> singleExtractor() {
        return rs -> {
            UserProfileDTO userProfile = null;
            while (rs.next()) {
                userProfile = this.mapToUserProfile(rs);
            }
            return Optional.ofNullable(userProfile);
        };
    }

    @Override
    public ResultSetExtractor<List<UserProfileDTO>> customListExtractor() {
        return rs -> {
            List<UserProfileDTO> userProfileList = new ArrayList<>();
            while (rs.next()) {
                UserProfileDTO userProfile = this.mapToUserProfile(rs);
                userProfileList.add(userProfile);
            }
            return userProfileList;
        };
    }

    private UserProfileDTO mapToUserProfile(ResultSet rs) throws SQLException {
        UserProfileDTO userProfile;
        userProfile = new UserProfileDTO();
        userProfile.setUsername(rs.getString("username"));
        userProfile.setFirstName(rs.getString("first_name"));
        userProfile.setLastName(rs.getString("last_name"));
        userProfile.setDateOfBirth(rs.getTimestamp("date_of_birth"));
        userProfile.setGender(Gender.fromValue(rs.getInt("gender")));
        userProfile.setPhoneNumber(rs.getString("phone_number"));
        userProfile.setEmail(rs.getString("email"));
        userProfile.setAvatarUrl(rs.getString("avatar_url"));
        userProfile.setOtherInfo(rs.getString("other_info"));
        userProfile.setOnline(false);
        return userProfile;
    }
}
