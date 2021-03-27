package com.lambda.dao.extractor.impl;

import com.lambda.constant.Gender;
import com.lambda.constant.SettingColumn;
import com.lambda.constant.UserColumn;
import com.lambda.constant.UserProfileColumn;
import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.dto.SettingDTO;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserExtractor implements SqlResultExtractor<UserDTO> {

    @Override
    public ResultSetExtractor<Optional<UserDTO>> singleExtractor() {
        return rs -> {
            UserDTO userDTO = null;
            String previousName = null;
            while (rs.next()) {
                String username = rs.getString(UserColumn.USERNAME);
                if (previousName != null && !previousName.equals(username)) break;
                if (!username.equals(previousName)) {
                    userDTO = new UserDTO(
                            null, null, username,
                            rs.getString(UserColumn.PASSWORD),
                            rs.getBoolean(UserColumn.ENABLED),
                            !rs.getBoolean(UserColumn.ACCOUNT_EXPIRED),
                            !rs.getBoolean(UserColumn.ACCOUNT_LOCKED),
                            !rs.getBoolean(UserColumn.CREDENTIALS_EXPIRED),
                            new HashSet<>());
                    if (rs.getString(UserColumn.USERNAME) != null) {
                        userDTO.setSetting(new SettingDTO(
                                rs.getString(UserColumn.USERNAME),
                                rs.getBoolean(SettingColumn.SETTING_ALERT),
                                rs.getString(SettingColumn.SETTING_THEME)));
                    }
                    if (rs.getString(UserColumn.USERNAME) != null) {
                        userDTO.setUserProfile(new UserProfileDTO(
                                rs.getString(UserColumn.USERNAME),
                                rs.getString(UserProfileColumn.FIRST_NAME),
                                rs.getString(UserProfileColumn.LAST_NAME),
                                rs.getTimestamp(UserProfileColumn.DATE_OF_BIRTH),
                                Gender.fromValue(rs.getInt(UserProfileColumn.GENDER)),
                                rs.getString(UserProfileColumn.PHONE_NUMBER),
                                rs.getString(UserProfileColumn.EMAIL),
                                rs.getString(UserProfileColumn.AVATAR_URL)));
                    }

                    previousName = username;
                }
                GrantedAuthority role = new SimpleGrantedAuthority(rs.getString(UserColumn.AUTHORITY));
                userDTO.getAuthorities().add(role);
            }
            return Optional.ofNullable(userDTO);
        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResultSetExtractor<List<UserDTO>> customListExtractor() {
        return rs -> {
            Map<String, Map<String, Object>> customUserMap = new HashMap<>();
            String previousName = null;
            while (rs.next()) {
                String username = rs.getString(UserColumn.USERNAME);
                if (!username.equals(previousName)) {
                    Map<String, Object> props = new HashMap<>();
                    props.put(UserColumn.USERNAME, username);
                    props.put(UserColumn.PASSWORD, rs.getString(UserColumn.PASSWORD));
                    props.put(UserColumn.ENABLED, rs.getBoolean(UserColumn.ENABLED));
                    props.put(UserColumn.ACCOUNT_EXPIRED, rs.getBoolean(UserColumn.ACCOUNT_EXPIRED));
                    props.put(UserColumn.ACCOUNT_LOCKED, rs.getBoolean(UserColumn.ACCOUNT_LOCKED));
                    props.put(UserColumn.CREDENTIALS_EXPIRED, rs.getBoolean(UserColumn.CREDENTIALS_EXPIRED));
                    props.put(UserColumn.SETTING, new SettingDTO(
                            rs.getString(UserColumn.USERNAME),
                            rs.getBoolean(SettingColumn.SETTING_ALERT),
                            rs.getString(SettingColumn.SETTING_THEME)));
                    props.put(UserColumn.AUTHORITY, new HashSet<>());
                    customUserMap.put(username, props);
                    previousName = username;
                }
                if (customUserMap.get(username) != null) {
                    GrantedAuthority role = new SimpleGrantedAuthority(rs.getString(UserColumn.AUTHORITY));
                    ((Set<GrantedAuthority>) customUserMap.get(username).get(UserColumn.AUTHORITY)).add(role);
                }
            }
            return customUserMap.values()
                    .stream()
                    .map(val -> {
                        UserDTO userDTO = new UserDTO(
                                (String) val.get(UserColumn.USERNAME),
                                (String) val.get(UserColumn.PASSWORD),
                                (boolean) val.get(UserColumn.ENABLED),
                                !(boolean) val.get(UserColumn.ACCOUNT_EXPIRED),
                                !(boolean) val.get(UserColumn.ACCOUNT_LOCKED),
                                !(boolean) val.get(UserColumn.CREDENTIALS_EXPIRED),
                                (Set<GrantedAuthority>) val.get(UserColumn.AUTHORITY));
                        userDTO.setSetting((SettingDTO) val.get(UserColumn.SETTING));
                        return userDTO;
                    })
                    .collect(Collectors.toList());
        };
    }

}
