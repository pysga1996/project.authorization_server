package com.lambda.dao;

import com.lambda.model.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Optional;

public interface UserDao extends UserDetailsManager {

    Page<UserDTO> findAll(Pageable pageable);

    Optional<UserDTO> findByUsername(String username);

    Page<UserDTO> findByUsernameContaining(String username, Pageable pageable);

    Page<UserDTO> findByAuthorities_Authority(String authority, Pageable pageable);

    Optional<UserDTO> findById(Long id);

    UserDTO findByEmail(String email);

    void saveAndFlush(UserDTO user);

    void deleteById(Long id);

    void save(UserDTO user);
}
