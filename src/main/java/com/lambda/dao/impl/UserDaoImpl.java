package com.lambda.dao.impl;

import com.lambda.constant.JdbcConstant;
import com.lambda.dao.UserDao;
import com.lambda.dao.extractor.SqlResultExtractor;
import com.lambda.model.domain.Group;
import com.lambda.model.domain.GroupInfo;
import com.lambda.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.lambda.constant.JdbcConstant.*;

@Component
@Transactional
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
    public void save(UserDTO user) {

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
        String sql= "SELECT ga.authority FROM `groups`\n" +
                "INNER JOIN group_authorities ga on `groups`.id = ga.group_id\n" +
                "WHERE id = ?\n" +
                "ORDER BY ga.authority LIMIT ?, ?";
        List<GrantedAuthority> authorities = this.jdbcOperations
                .queryForList(sql, String.class, id, offset, pageSize)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        String countSql= "SELECT COUNT(ga.authority) FROM `groups`\n" +
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
        String sql= "SELECT gm.username FROM `groups`\n" +
                "INNER JOIN group_members gm on `groups`.id = gm.group_id\n" +
                "WHERE id = ?\n" +
                "ORDER BY gm.username LIMIT ?, ?";
        List<String> authorities = this.jdbcOperations
                .queryForList(sql, String.class, id, offset, pageSize);
        String countSql= "SELECT COUNT(gm.username) FROM `groups`\n" +
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

}
