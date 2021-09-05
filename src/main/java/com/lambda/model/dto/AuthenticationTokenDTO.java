package com.lambda.model.dto;

import com.lambda.constant.TokenStatus;
import java.sql.Timestamp;
import lombok.Data;

@Data
public class AuthenticationTokenDTO {

    private Long id;

    private String token;

    private String username;

    private Timestamp createDate;

    private Timestamp expireDate;

    private String redirectUrl;

    private TokenStatus status;
}
