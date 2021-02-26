package com.lambda.dao.impl;

import com.lambda.constant.CommonConstant;
import com.lambda.constant.ErrorCode;
import com.lambda.constant.JdbcConstant;
import com.lambda.dao.UserDao;
import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.error.BusinessException;
import com.lambda.model.domain.Group;
import com.lambda.model.domain.GroupInfo;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

import static com.lambda.constant.JdbcConstant.*;

@Log4j2
@Component
public class UserDaoImpl extends JdbcUserDetailsManager implements UserDao {

    private final JdbcOperations jdbcOperations;

    private final SqlResultExtractor<UserDTO> extractor;

    @Autowired
    public UserDaoImpl(JdbcOperations jdbcOperations, DataSource dataSource,
                       SqlResultExtractor<UserDTO> extractor) {
        this.extractor = extractor;
        this.jdbcOperations = jdbcOperations;
        this.setDataSource(dataSource);
        this.setFindAllGroupsSql(DEF_CUSTOM_FIND_GROUPS_SQL);
        this.setFindGroupIdSql(DEF_CUSTOM_FIND_GROUP_ID_SQL);
        this.setInsertGroupSql(DEF_CUSTOM_INSERT_GROUP_SQL);
        this.setRenameGroupSql(DEF_CUSTOM_RENAME_GROUP_SQL);
        this.setDeleteGroupSql(DEF_CUSTOM_DELETE_GROUP_SQL);
    }

    @Override
    public Page<UserDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<Group> findGroupList() {
        String sql = "SELECT id, group_name FROM `groups`";
        return this.jdbcOperations.query(sql, rs -> {
            List<Group> groupList = new ArrayList<>();
            while (rs.next()) {
                Group group = new Group();
                group.setId(rs.getLong("id"));
                group.setName(rs.getString("group_name"));
                groupList.add(group);
            }
            return groupList;
        });
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        return this.jdbcOperations.query(
                JdbcConstant.DEF_USERS_BY_USERNAME_FULL_WITH_SETTING_QUERY,
                extractor.singleExtractor(), username);
    }

    @Override
    public Page<UserDTO> findByUsernameContaining(String username, Pageable pageable) {
        return null;
    }

    @Override
    public Page<UserDTO> findByAuthorities_Authority(String authority, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        return this.jdbcOperations.query(
                JdbcConstant.DEF_USERS_BY_ID_FULL_WITH_SETTING_QUERY,
                extractor.singleExtractor(), id);
    }

    @Override
    public UserDTO findByEmail(String email) {
        return null;
    }

    @Override
    public void saveAndFlush(UserDTO user) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void saveProfile(UserProfileDTO userProfileDTO) {

    }

    @Override
    public List<GroupInfo> groupListWithCountInfo() {
        String sql = "SELECT `groups`.id AS id, group_name as name,\n" +
                "       IFNULL(members.user_count, 0) AS usersCount,\n" +
                "       IFNULL(authorities.authority_count, 0) AS authoritiesCount\n" +
                "FROM `groups`\n" +
                "LEFT JOIN (\n" +
                "    SELECT group_id, COUNT(DISTINCT username) AS user_count FROM group_members GROUP BY group_id\n" +
                ") AS members ON members.group_id = `groups`.id\n" +
                "LEFT JOIN (\n" +
                "    select group_id, COUNT(DISTINCT authority) AS authority_count FROM group_authorities GROUP BY group_id\n" +
                ") AS authorities ON authorities.group_id = `groups`.id";
        return this.jdbcOperations.query(sql, new BeanPropertyRowMapper<>(GroupInfo.class));
    }


    @Override
    public Group findGroupById(Long id) {
        String sql = "SELECT `groups`.id, group_name FROM `groups`\n" +
                " WHERE `groups`.id = ?";
        return this.jdbcOperations.queryForObject(sql, (rs, rowNum) -> {
            Group group = new Group();
            group.setId(rs.getLong("id"));
            group.setName(rs.getString("group_name"));
            return group;
        }, id);
    }

    @Override
    public Page<GrantedAuthority> findGroupAuthorityPageById(Long id, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        String sql = "SELECT ga.authority FROM `groups`\n" +
                "INNER JOIN group_authorities ga on `groups`.id = ga.group_id\n" +
                "WHERE id = ?\n" +
                "ORDER BY ga.authority LIMIT ?, ?";
        List<GrantedAuthority> authorities = this.jdbcOperations
                .queryForList(sql, String.class, id, offset, pageSize)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        String countSql = "SELECT COUNT(ga.authority) FROM `groups`\n" +
                "INNER JOIN group_authorities ga on `groups`.id = ga.group_id\n" +
                "WHERE id = ?";
        Long count = this.jdbcOperations.queryForObject(countSql, Long.class, id);
        if (count == null) {
            count = 0L;
        }
        return new PageImpl<>(authorities, pageable, count);
    }

    @Override
    public Page<String> findGroupUserPageById(Long id, Pageable pageable) {
        long offset = pageable.getOffset();
        int pageSize = pageable.getPageSize();
        String sql = "SELECT gm.username FROM `groups`\n" +
                "INNER JOIN group_members gm on `groups`.id = gm.group_id\n" +
                "WHERE id = ?\n" +
                "ORDER BY gm.username LIMIT ?, ?";
        List<String> authorities = this.jdbcOperations
                .queryForList(sql, String.class, id, offset, pageSize);
        String countSql = "SELECT COUNT(gm.username) FROM `groups`\n" +
                "INNER JOIN group_members gm on `groups`.id = gm.group_id\n" +
                "WHERE id = ?";
        Long count = this.jdbcOperations.queryForObject(countSql, Long.class, id);
        if (count == null) {
            count = 0L;
        }
        return new PageImpl<>(authorities, pageable, count);
    }

    @Override
    public Boolean checkGroupExistForCreate(String groupName) {
        String sql = "SELECT EXISTS(SELECT * FROM `groups` WHERE group_name = ?)";
        return this.jdbcOperations.queryForObject(sql, Boolean.class, groupName);
    }

    @Override
    public String checkGroupExistForUpdate(String groupName, Long groupId) {
        String sql = "SELECT group_name FROM `groups` WHERE id = ?";
        return this.jdbcOperations.queryForObject(sql, String.class, groupName);
    }

    @Override
    public boolean checkAuthorityExistInGroup(String groupName, String authorities) {
        String sql = "SELECT COUNT(DISTINCT authority) FROM `groups` " +
                " INNER JOIN group_authorities ON group_authorities.group_id = `groups`.id " +
                " WHERE group_name = ? AND authority = ?";
        Long count = this.jdbcOperations.queryForObject(sql, Long.class, groupName, authorities);
        return count != null && count > 0;
    }

    @Override
    public boolean checkUserExistInGroup(String groupName, String usernames) {
        String sql = "SELECT COUNT(DISTINCT username) FROM `groups` " +
                " INNER JOIN group_members ON group_members.group_id = `groups`.id" +
                " WHERE group_name = ? AND username IN ?";
        Long count = this.jdbcOperations.queryForObject(sql, Long.class, groupName, usernames);
        return count != null && count > 0;
    }

    @Override
    public void register(UserDTO userDTO) {
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(Objects.requireNonNull(this.getDataSource()))
                    .withProcedureName("REGISTER");
            MapSqlParameterSource in = new MapSqlParameterSource()
                    .addValue("username", userDTO.getUsername())
                    .addValue("password", userDTO.getPassword())
                    .addValue("first_name", userDTO.getUserProfile().getFirstName())
                    .addValue("last_name", userDTO.getUserProfile().getLastName())
                    .addValue("gender", userDTO.getUserProfile().getGender().getValue())
                    .addValue("date_of_birth", userDTO.getUserProfile().getDateOfBirth())
                    .addValue("email", userDTO.getUserProfile().getEmail())
                    .addValue("phone_number", userDTO.getUserProfile().getPhoneNumber())
                    .addValue("group_name", CommonConstant.DEFAULT_GROUP);
            simpleJdbcCall.execute(in);
        } catch (UncategorizedSQLException ex) {
            SQLException sqlException = ex.getSQLException();
            if (CUSTOM_SQL_STATE.equals(sqlException.getSQLState())) {
                switch (ErrorCode.get(sqlException.getMessage())) {
                    case USERNAME_EXISTED:
                        throw new BusinessException(sqlException.getErrorCode(), "validation.username.existed");
                    case USER_PROFILE_EXISTED:
                        throw new BusinessException(sqlException.getErrorCode(), "validation.userProfile.existed");
                    case GROUP_NOT_FOUND:
                        throw new BusinessException(sqlException.getErrorCode(), "validation.group.notFound");
                }
            }
            throw ex;
        }
    }

    @Override
    public void unRegister(String username) {
        try {
            SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(Objects.requireNonNull(this.getDataSource()))
                    .withProcedureName("UNREGISTER");
            MapSqlParameterSource in = new MapSqlParameterSource()
                    .addValue("username", username);
            simpleJdbcCall.execute(in);
        } catch (UncategorizedSQLException ex) {
            SQLException sqlException = ex.getSQLException();
            if (CUSTOM_SQL_STATE.equals(sqlException.getSQLState())) {
                if (ErrorCode.get(sqlException.getMessage()) == ErrorCode.USERNAME_NOT_FOUND) {
                    throw new BusinessException(sqlException.getErrorCode(), "validation.username.notFound");
                }
            }
            throw ex;
        }
    }

    @Override
    public Page<UserDTO> userList(Pageable pageable) {
        String sql = "SELECT user.id, user.username, password, enabled, account_locked,\n" +
                "       account_expired, credentials_expired, `groups`.id AS group_id, `groups`.group_name\n" +
                "FROM user\n" +
                "LEFT JOIN group_members ON group_members.username = user.username\n" +
                "LEFT JOIN `groups` ON `groups`.id = group_members.group_id\n" +
                "LIMIT ?, ?";
        List<UserDTO> userList = this.jdbcOperations.query(sql, rs -> {
            UserDTO user;
            long id;
            Map<Long, UserDTO> userMap = new HashMap<>();
            while (rs.next()) {
                id = rs.getLong("id");
                if (userMap.containsKey(id)) {
                    user = userMap.get(id);
                } else {
                    user = new UserDTO();
                    user.setId(id);
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEnabled(rs.getBoolean("enabled"));
                    user.setAccountNonLocked(!rs.getBoolean("account_locked"));
                    user.setAccountNonExpired(!rs.getBoolean("account_expired"));
                    user.setCredentialsNonExpired(!rs.getBoolean("credentials_expired"));
                    user.setGroupList(new HashSet<>());
                    userMap.put(id, user);
                }
                if (rs.getString("group_name") != null) {
                    Group group = new Group();
                    group.setId(rs.getLong("group_id"));
                    group.setName(rs.getString("group_name"));
                    user.getGroupList().add(group);
                }
            }
            return userMap.values()
                    .stream()
                    .sorted(Comparator.comparing(UserDTO::getUsername))
                    .collect(Collectors.toList());
        }, pageable.getOffset(), pageable.getPageSize());
        if (userList == null) userList = new ArrayList<>();
        String countSql = "SELECT COUNT(user.username) \n" +
                "FROM user\n" +
                "LEFT JOIN group_members ON group_members.username = user.username\n" +
                "LEFT JOIN `groups` ON `groups`.id = group_members.group_id ";
        Long count = this.jdbcOperations.queryForObject(countSql, Long.class);
        return new PageImpl<>(userList, pageable, count == null ? 0 : count);
    }

    @Override
    public Set<Long> findGroupIdSetByUsername(String username) {
        String sql = "SELECT group_id FROM group_members WHERE username = ?";
        return this.jdbcOperations.query(sql, rs -> {
            Set<Long> ids = new HashSet<>();
            while (rs.next()) {
                ids.add(rs.getLong("group_id"));
            }
            return ids;
        }, username);
    }

    @Override
    public void updateUserAndGroup(String username, String newPassword, boolean enabled, boolean accountLocked, boolean accountExpired,
                                   boolean credentialsExpired, Set<Long> insertGroups, Set<Long> deleteGroups) {
        String updateSql;
        if (newPassword != null) {
            updateSql = "UPDATE user SET password = ?, enabled = ?, account_locked = ?, \n" +
                    "account_expired = ?, credentials_expired = ? WHERE username = ?";
        } else {
            updateSql = "UPDATE user SET enabled = ?, account_locked = ?, \n" +
                    "account_expired = ?, credentials_expired = ? WHERE username = ?";
        }

        Object[] params;
        int[] argTypes;
        if (newPassword != null) {
            params = new Object[]{newPassword, enabled, accountLocked, accountExpired, credentialsExpired, username};
            argTypes = new int[]{Types.VARCHAR, Types.BIT, Types.BIT, Types.BIT, Types.BIT, Types.VARCHAR};
        } else {
            params = new Object[]{enabled, accountLocked, accountExpired, credentialsExpired, username};
            argTypes = new int[]{Types.BIT, Types.BIT, Types.BIT, Types.BIT, Types.VARCHAR};
        }
        this.jdbcOperations.update(updateSql, params, argTypes);
        if (!deleteGroups.isEmpty()) {
            String deleteSql = "DELETE FROM group_members WHERE group_id IN (%s) AND username = ?";
            String ids = String.join(",", deleteGroups.stream()
                    .map(String::valueOf)
                    .collect(Collectors.toSet()));
            this.jdbcOperations.update(String.format(deleteSql, ids), username);
        }
        if (!insertGroups.isEmpty()) {
            String insertSql = "INSERT INTO group_members (username, group_id) VALUES (?, ?)";
            this.jdbcOperations.batchUpdate(insertSql, insertGroups, 10, (ps, argument) -> {
                ps.setString(1, username);
                ps.setLong(2, argument);
            });
        }
    }
}
