package com.lambda.dao.extractor;

import com.lambda.constant.SettingColumn;
import com.lambda.constant.UserColumn;
import com.lambda.model.CustomUser;
import com.lambda.model.Setting;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserResultSetExtractor implements ResultSetExtractor<List<CustomUser>> {

    @Override
    @SuppressWarnings("unchecked")
    public List<CustomUser> extractData(ResultSet rs) throws SQLException, DataAccessException {
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
                props.put(UserColumn.AVATAR_URL, rs.getString(UserColumn.AVATAR_URL));
                props.put(UserColumn.SETTING, new Setting(rs.getLong(SettingColumn.SETTING_ID),
                        rs.getBoolean(SettingColumn.SETTING_DARK_MODE)));
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
                    CustomUser customUser = new CustomUser(
                            (String) val.get(UserColumn.USERNAME),
                            (String) val.get(UserColumn.PASSWORD),
                            (boolean) val.get(UserColumn.ENABLED),
                            !(boolean) val.get(UserColumn.ACCOUNT_EXPIRED),
                            !(boolean) val.get(UserColumn.ACCOUNT_LOCKED),
                            !(boolean) val.get(UserColumn.CREDENTIALS_EXPIRED),
                            (Set<GrantedAuthority>) val.get(UserColumn.AUTHORITY));
                    customUser.setId(e.getKey());
                    customUser.setAvatarUrl((String) val.get(UserColumn.AVATAR_URL));
                    customUser.setSetting((Setting) val.get(UserColumn.SETTING));
                    return customUser;
                })
                .collect(Collectors.toList());

    }
}
