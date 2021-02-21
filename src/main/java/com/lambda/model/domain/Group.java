package com.lambda.model.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group implements Serializable {

    private Long id;

    private String name;

    private List<GrantedAuthority> authorities;

    private Page<GrantedAuthority> authorityPage;

    private Page<String> userPage;
}
