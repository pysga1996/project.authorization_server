package com.lambda.model.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordDTO {

    @NotBlank
    @Email
    String email;

    String redirectUrl;
}
