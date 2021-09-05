package com.lambda.model.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.jackson.JsonComponent;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonComponent
public class SettingDTO implements Serializable {

    private static final long serialVersionUID = 43L;

    private String username;

    @Builder.Default
    private Boolean alert = true;

    private String theme;
}
