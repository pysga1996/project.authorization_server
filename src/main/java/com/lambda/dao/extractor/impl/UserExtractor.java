package com.lambda.dao.extractor.impl;

import com.lambda.constant.SettingColumn;
import com.lambda.constant.UserColumn;
import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.dto.SettingDTO;
import com.lambda.model.dto.UserDTO;
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
            Long previousId = -1L;
            while (rs.next()) {
                Long id = rs.getLong(UserColumn.ID);
                if (!id.equals(previousId)) {
                    userDTO = new UserDTO(
                            id, null, null,
                            rs.getString(UserColumn.USERNAME),
                            rs.getString(UserColumn.PASSWORD),
                            rs.getBoolean(UserColumn.ENABLED),
                            !rs.getBoolean(UserColumn.ACCOUNT_EXPIRED),
                            !rs.getBoolean(UserColumn.ACCOUNT_LOCKED),
                            !rs.getBoolean(UserColumn.CREDENTIALS_EXPIRED),
                            new HashSet<>());
                    previousId = id;
                }
                if (userDTO != null) {
                    GrantedAuthority role = new SimpleGrantedAuthority(rs.getString(UserColumn.AUTHORITY));
                    userDTO.getAuthorities().add(role);
                }
            }
            return Optional.ofNullable(userDTO);

        };
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResultSetExtractor<List<UserDTO>> listExtractor() {
        return rs -> {
            Map<Long, Map<String, Object>> customUserMap = new HashMap<>();
            Long previousId = -1L;
            while (rs.next()) {
                Long id = rs.getLong(UserColumn.ID);
                if (!id.equals(previousId)) {
                    Map<String, Object> props = new HashMap<>();
                    props.put(UserColumn.USERNAME, rs.getString(UserColumn.USERNAME));
                    props.put(UserColumn.PASSWORD, rs.getString(UserColumn.PASSWORD));
                    props.put(UserColumn.ENABLED, rs.getBoolean(UserColumn.ENABLED));
                    props.put(UserColumn.ACCOUNT_EXPIRED, rs.getBoolean(UserColumn.ACCOUNT_EXPIRED));
                    props.put(UserColumn.ACCOUNT_LOCKED, rs.getBoolean(UserColumn.ACCOUNT_LOCKED));
                    props.put(UserColumn.CREDENTIALS_EXPIRED, rs.getBoolean(UserColumn.CREDENTIALS_EXPIRED));
                    props.put(UserColumn.SETTING, new SettingDTO(rs.getLong(SettingColumn.SETTING_ID),
                            id, rs.getBoolean(SettingColumn.SETTING_DARK_MODE)));
                    props.put(UserColumn.AUTHORITY, new HashSet<>());
                    customUserMap.put(id, props);
                    previousId = id;
                }
                if (customUserMap.get(id) != null) {
                    GrantedAuthority role = new SimpleGrantedAuthority(rs.getString(UserColumn.AUTHORITY));
                    ((Set<GrantedAuthority>) customUserMap.get(id).get(UserColumn.AUTHORITY)).add(role);
                }
            }
            return customUserMap.entrySet()
                    .stream()
                    .map(e -> {
                        Map<String, Object> val = e.getValue();
                        UserDTO userDTO = new UserDTO(
                                (String) val.get(UserColumn.USERNAME),
                                (String) val.get(UserColumn.PASSWORD),
                                (boolean) val.get(UserColumn.ENABLED),
                                !(boolean) val.get(UserColumn.ACCOUNT_EXPIRED),
                                !(boolean) val.get(UserColumn.ACCOUNT_LOCKED),
                                !(boolean) val.get(UserColumn.CREDENTIALS_EXPIRED),
                                (Set<GrantedAuthority>) val.get(UserColumn.AUTHORITY));
                        userDTO.setId(e.getKey());
                        userDTO.setSetting((SettingDTO) val.get(UserColumn.SETTING));
                        return userDTO;
                    })
                    .collect(Collectors.toList());

        };
    }

}
