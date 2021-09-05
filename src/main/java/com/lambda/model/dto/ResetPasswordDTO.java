package com.lambda.model.dto;

import com.lambda.validator.constraints.RepeatedPasswordConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ResetPasswordDTO {

    @NotBlank
    @Email
    String email;

    String redirectUrl;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @RepeatedPasswordConstraint
    public static class ChangePasswordConfirmDTO {

        @NotBlank(groups = {ResetPasswordCase.class})
        private String email;

        @NotBlank(groups = {ResetPasswordCase.class})
        private String token;

        @NotBlank(groups = {UpdatePasswordCase.class})
        private String oldPassword;

        @NotBlank(groups = {ResetPasswordCase.class, UpdatePasswordCase.class})
        private String newPassword;

        @NotBlank(groups = {ResetPasswordCase.class, UpdatePasswordCase.class})
        private String repeatedNewPassword;
    }

    public interface ResetPasswordCase {}
    public interface UpdatePasswordCase {}
}
