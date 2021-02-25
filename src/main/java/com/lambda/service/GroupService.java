package com.lambda.service;

import com.lambda.model.domain.Group;
import com.lambda.model.domain.GroupInfo;

import java.util.List;

public interface GroupService {

    List<Group> groupList();

    List<GroupInfo> groupListWithInfo();

    Group findGroupById(Long id, int authorityPageNumber, int authorityPageSize,
                        int userPageNumber, int userPageSize);

    void createGroup(Group group);

    void renameGroup(Group group);

    void addAuthorityToGroup(String groupName, String authority);

    void addUserToGroup(String groupName, String authority);

    void removeAuthority(String groupName, String authority);
}
