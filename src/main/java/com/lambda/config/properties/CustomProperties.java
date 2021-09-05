package com.lambda.config.properties;

import java.util.Locale;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author thanhvt
 * @created 22/08/2021 - 3:01 SA
 * @project vengeance
 * @since 1.0
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "custom")
public class CustomProperties {

    private String httpPort;

    private String httpsPort;

    private SecurityPolicy securityPolicy;

    private ConnectorScheme connectorScheme;

    private TrustStoreType trustStoreType;

    private String trustStorePassword;

    public enum SecurityPolicy {
        CONFIDENTIAL, INTEGRAL, NONE
    }

    public enum ConnectorScheme {
        HTTP, HTTPS;

        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }

    public enum TrustStoreType {
        JKS, PKCS12;

        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
