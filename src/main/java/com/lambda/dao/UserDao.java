package com.lambda.dao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lambda.model.domain.Group;
import com.lambda.model.domain.GroupInfo;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

public interface UserDao extends UserDetailsManager, GroupManager {

    Page<UserDTO> findAll(Pageable pageable);

    List<Group> findGroupList();

    Optional<UserDTO> findByUsername(String username);

    Map<String, Object> getCurrentUserShortInfo(String username);

    Page<UserDTO> findByUsernameContaining(String username, Pageable pageable);

    Page<UserDTO> findByAuthorities_Authority(String authority, Pageable pageable);

    UserDTO findByEmail(String email);

    void saveAndFlush(UserDTO user);

    void deleteById(Long id);

    void saveProfile(UserProfileDTO userProfileDTO);

    List<GroupInfo> groupListWithCountInfo();

    Group findGroupById(Long id);

    Page<GrantedAuthority> findGroupAuthorityPageById(Long id, Pageable pageable);

    Page<String> findGroupUserPageById(Long id, Pageable pageable);

    Boolean checkGroupExistForCreate(String groupName);

    String checkGroupExistForUpdate(String groupName, Long groupId);

    boolean checkAuthorityExistInGroup(String groupName, String authorities);

    boolean checkUserExistInGroup(String groupName, String usernames);

    void register(UserDTO userDTO);

    void unRegister(String username);

    Map<String, UserProfileDTO> userList();

    Page<UserDTO> userList(Pageable pageable);

    List<Group> findGroupListByUsername(String username);

    Set<Long> findGroupIdSetByUsername(String username);

    void updateUserAndGroup(String username, String newPassword, boolean enabled,
        boolean accountLocked, boolean accountExpired,
        boolean credentialsExpired, Set<Long> insertGroups, Set<Long> deleteGroups);

    void updateOtherInfo(String username, Map<String, Object> infoJson)
        throws JsonProcessingException;

    boolean resetPassword(String username, String newPassword);
}
