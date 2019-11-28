package com.lambda.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Setting implements Serializable {
    private static final long serialVersionUID = 42L;

    private Long id;

    @Builder.Default
    private Boolean darkMode = true;
}
