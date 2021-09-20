package com.lambda.constant;

import org.springframework.stereotype.Component;

/**
 * @author thanhvt
 * @created 20/09/2021 - 2:36 CH
 * @project vengeance
 * @since 1.0
 **/
@Component("roleConstants")
public class RoleConstants {

    public static final String USER_MANAGEMENT = "user-management";

    public static final String CLIENT_MANAGEMENT = "client-management";

    public static final String GROUP_MANAGEMENT = "group-management";

    public static final String AUTHORITY_MANAGEMENT = "authority-management";
}
