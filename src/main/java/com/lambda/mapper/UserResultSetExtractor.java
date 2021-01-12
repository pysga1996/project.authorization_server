package com.lambda.mapper;

import com.lambda.model.Setting;
import com.lambda.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserResultSetExtractor implements ResultSetExtractor<List<User>> {

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> users = new ArrayList<>();
        Set<GrantedAuthority> roles = new HashSet<>();
        Long previousId = -1L;
        User currentUser = null;
        while (rs.next()) {
            Long id = rs.getLong("id");

            if (!id.equals(previousId)) {
                String password = rs.getString("password");
                String username = rs.getString("username");
                boolean accountNonExpired = rs.getBoolean("account_non_expired");
                boolean accountNonLocked = rs.getBoolean("account_non_locked");
                boolean credentialsNonExpired = rs.getBoolean("credentials_non_expired");
                boolean enabled = rs.getBoolean("enabled");
                String avatarUrl = rs.getString("avatar_url");
                currentUser = new User(id, password, username, null, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled, avatarUrl, null);
                users.add(currentUser);
                previousId = id;
            }

            if (currentUser != null) {
                GrantedAuthority role = new SimpleGrantedAuthority(rs.getString("role_authority"));
                roles.add(role);
                currentUser.setAuthorities(roles);
                Setting setting = new Setting(rs.getLong("setting_id"), rs.getBoolean("setting_dark_mode"));
                currentUser.setSetting(setting);
            }
        }

        return users;

    }
}
