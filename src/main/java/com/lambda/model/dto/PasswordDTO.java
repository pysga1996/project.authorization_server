package com.lambda.model.dto;

import com.lambda.validator.constraints.RepeatedPasswordConstraint;
import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;

@Data
@RepeatedPasswordConstraint
public class PasswordDTO {

    @Nullable
    private String oldPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String repeatedNewPassword;
}
