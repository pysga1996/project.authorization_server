package com.lambda.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupInfo {

    private Long id;

    private String name;

    private Long usersCount;

    private Long authoritiesCount;
}
