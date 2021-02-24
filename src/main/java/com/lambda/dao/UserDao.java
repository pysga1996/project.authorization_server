package com.lambda.dao;

import com.lambda.model.domain.Group;
import com.lambda.model.domain.GroupInfo;
import com.lambda.model.dto.UserDTO;
import com.lambda.model.dto.UserProfileDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.provisioning.GroupManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;
import java.util.Optional;

public interface UserDao extends UserDetailsManager, GroupManager {

    Page<UserDTO> findAll(Pageable pageable);

    Optional<UserDTO> findByUsername(String username);

    Page<UserDTO> findByUsernameContaining(String username, Pageable pageable);

    Page<UserDTO> findByAuthorities_Authority(String authority, Pageable pageable);

    Optional<UserDTO> findById(Long id);

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

    Page<UserDTO> userList(Pageable pageable);
}
