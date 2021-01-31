package com.lambda.model.dto;

import com.lambda.constant.Gender;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
public class UserProfileDTO {

    private Long id;

    private Long userId;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private Gender gender;

    private String phoneNumber;

    @Email
    @NotBlank
    private String email;

    private String avatarUrl;

    private String avatarBlobString;
}
