package com.lambda.model.dto;

import com.lambda.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO implements Serializable {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private Timestamp dateOfBirth;

    private Gender gender;

    private String phoneNumber;

    @Email
    @NotBlank
    private String email;

    private String avatarUrl;

}
