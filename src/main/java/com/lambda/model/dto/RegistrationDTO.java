package com.lambda.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {

    @NotNull
    private UserDTO userDTO;

    @NotBlank
    private String redirectUrl;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RegistrationConfirmDTO {

        @NotBlank
        private String email;

        @NotBlank
        private String token;
    }
}
