package com.lambda.service.impl;

import com.lambda.dao.UserDao;
import com.lambda.error.BusinessException;
import com.lambda.model.domain.Group;
import com.lambda.model.domain.GroupInfo;
import com.lambda.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final UserDao userDao;

    @Autowired
    public GroupServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Group> groupList() {
        return this.userDao.findGroupList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupInfo> groupListWithInfo() {
        return this.userDao.groupListWithCountInfo();
    }

    @Override
    @Transactional(readOnly = true)
    public Group findGroupById(Long id, int authorityPageNumber, int authorityPageSize,
                               int userPageNumber, int userPageSize) {
        Group group = this.userDao.findGroupById(id);
        if (group == null) {
            throw new BusinessException(404, "Group not found");
        }
        Pageable authorityPageable = PageRequest
                .of(authorityPageNumber - 1, authorityPageSize);
        Page<GrantedAuthority> authorityPage = this.userDao
                .findGroupAuthorityPageById(id, authorityPageable);
        group.setAuthorityPage(authorityPage);
        Pageable userPageable = PageRequest
                .of(authorityPageNumber - 1, userPageSize);
        Page<String> userPage = this.userDao
                .findGroupUserPageById(id, userPageable);
        group.setUserPage(userPage);
        return group;
    }

    @Override
    @Transactional
    public void createGroup(Group group) {
        Boolean isGroupExisted = this.userDao.checkGroupExistForCreate(group.getName());
        if (Boolean.TRUE.equals(isGroupExisted)) {
            throw new BusinessException(409, "Group existed");
        }
        List<GrantedAuthority> authorities = group.getAuthorities() == null ?
                new ArrayList<>() : group.getAuthorities();
        this.userDao.createGroup(group.getName(), authorities);
    }

    @Override
    @Transactional
    public void renameGroup(Group group) {
        String oldName = this.userDao.checkGroupExistForUpdate(group.getName(), group.getId());
        this.userDao.renameGroup(oldName, group.getName());
    }

    @Override
    @Transactional
    public void addAuthorityToGroup(String groupName, String authority) {
        boolean authorityExistInGroup = this.userDao.checkAuthorityExistInGroup(groupName, authority);
        if (authorityExistInGroup) {
            throw new BusinessException(409,
                    String.format("Authority %s existed in group %s", authority, groupName));
        }
        this.userDao.addGroupAuthority(groupName, new SimpleGrantedAuthority(authority));
    }

    @Override
    @Transactional
    public void addUserToGroup(String groupName, String username) {
        boolean userExistInGroup = this.userDao.checkUserExistInGroup(groupName, username);
        if (userExistInGroup) {
            throw new BusinessException(409,
                    String.format("Users %s existed in group %s", username, groupName));
        }
        this.userDao.addUserToGroup(username, groupName);
    }

    @Override
    @Transactional
    public void removeAuthority(String groupName, String authority) {
        boolean authorityExistInGroup = this.userDao.checkAuthorityExistInGroup(groupName, authority);
        if (!authorityExistInGroup) {
            throw new BusinessException(409,
                    String.format("Authority %s does not exist in group %s", authority, groupName));
        }
        this.userDao.removeGroupAuthority(groupName, new SimpleGrantedAuthority(authority));
    }

    @Override
    @Transactional
    public void removeGroup(String groupName) {
        this.userDao.deleteGroup(groupName);
    }
}
