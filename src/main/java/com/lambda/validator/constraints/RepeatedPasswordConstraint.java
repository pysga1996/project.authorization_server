package com.lambda.validator.constraints;

import com.lambda.model.dto.ResetPasswordDTO.UpdatePasswordCase;
import com.lambda.validator.RepeatedPasswordValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RepeatedPasswordValidator.class)
@Documented
public @interface RepeatedPasswordConstraint {

    String message() default "Repeated password does not match!";

    Class<?>[] groups() default {UpdatePasswordCase.class};

    Class<? extends Payload>[] payload() default {};
}
