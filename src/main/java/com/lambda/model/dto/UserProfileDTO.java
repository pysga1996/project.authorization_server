package com.lambda.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRawValue;
import com.lambda.constant.Gender;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"url", "blobString"})
public class UserProfileDTO extends UploadDTO implements Serializable {

    private String username;

    @NotBlank(message = "{validation.firstName.notBlank}")
    private String firstName;

    @NotBlank(message = "{validation.lastName.notBlank}")
    private String lastName;

    @NotNull(message = "{validation.dateOfBirth.notNull}")
    private Timestamp dateOfBirth;

    @NotNull(message = "{validation.gender.notNull}")
    private Gender gender;

    @Pattern(regexp = "^(\\s*|(\\+84|84|0)+([3|5|7|8|9])([0-9]{8}))$",
        message = "{validation.phoneNumber.invalid}")
    private String phoneNumber;

    @Email(message = "{validation.email.invalid}")
    private String email;

    private String avatarUrl;

    private String avatarBlobString;

    private boolean isOnline;

    @JsonRawValue
    private String otherInfo;

    public UserProfileDTO(String username, String firstName, String lastName,
        Timestamp dateOfBirth, Gender gender, String phoneNumber,
        String email, String avatarUrl) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }

    @Override
    public String getUrl() {
        return this.avatarUrl;
    }

    @Override
    public String createFileName(String ext) {
        return this.username.concat(String.valueOf(new Date().getTime())).concat(".").concat(ext);
    }

    @Override
    public String getFolder() {
        return "avatar";
    }

    @Override
    public String getBlobString() {
        return avatarBlobString;
    }

    @Override
    public void setBlobString(String blobString) {
        this.avatarBlobString = blobString;
    }
}
