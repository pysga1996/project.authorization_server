package com.lambda.model.dto;

import com.lambda.constant.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDTO implements Serializable {

    private Long id;

    private Long userId;

    @NotBlank(message = "{validation.firstName.notBlank}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.notBlank}")
    private String lastName;

    @NotNull(message = "{validation.dateOfBirth.notNull}")
    private Timestamp dateOfBirth;

    @NotNull(message = "{validation.gender.notNull}")
    private Gender gender;

    @Pattern(regexp = "^(\\s*|(\\+84|84|0)+([3|5|7|8|9])([0-9]{8}))$", message = "{validation.phoneNumber.invalid}")
    private String phoneNumber;

    @Email(message = "{validation.email.invalid}")
    private String email;

    private String avatarUrl;

}
