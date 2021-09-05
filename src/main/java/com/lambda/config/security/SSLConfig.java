package com.lambda.config.security;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import lombok.extern.log4j.Log4j2;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.cloud.CloudPlatform;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Configuration
public class SSLConfig {

    private final Environment env;
    @Value("classpath:vengeance.jks")
    private Resource trustStore;
    @Value("${custom.trust-store-password}")
    private String trustStorePassword;
    @Value("${custom.trust-store-type}")
    private String trustStoreType;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;

    @Autowired
    public SSLConfig(Environment env) {
        this.env = env;
    }

    //    static {
//        //for localhost testing only
////        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
////                (hostname, sslSession) -> hostname.equals("localhost");
//        System.setProperty("https.protocols", "TLSv1.2");
//        ClassPathResource resource = new ClassPathResource("vengeance.jks");
//        try {
//            System.setProperty("javax.net.ssl.trustStore", Paths.get(resource.getURI()).toString());
//            System.setProperty("javax.net.ssl.trustStorePassword", "1381996");
//            System.setProperty("javax.net.ssl.trustStoreType", "JKS");
//        } catch (IOException e) {
//            log.error(e);
//            System.exit(1);
//        }
//    }

//    @PostConstruct
//    private void configureSSL() throws IOException {
////        System.setProperty("javax.net.debug", "ssl");
//        System.setProperty("https.protocols", "TLSv1.2");
//        System.setProperty("javax.net.ssl.trustStore", Paths.get(trustStore.getURI()).toString());
//        System.setProperty("javax.net.ssl.trustStorePassword", Objects.requireNonNull(trustStorePassword));
//        System.setProperty("javax.net.ssl.trustStoreType", Objects.requireNonNull(trustStoreType));
//    }

    //    @PostConstruct
    public SSLContext customSSL() {
        try {
            HttpsURLConnection
                .setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));
            //Gets the inputstream of a a trust store file under ssl/rpgrenadesClient.jks
            //This path refers to the ssl folder in the jar file, in a jar file in the same directory
            //as this jar file, or a different directory in the same directory as the jar file
            InputStream stream = this.trustStore.getInputStream();
            //Both trustStores and keyStores are represented by the KeyStore object
            KeyStore trustStore = KeyStore.getInstance(this.trustStoreType.toLowerCase());
            //The password for the trustStore
            //This loads the trust store into the object
            trustStore.load(stream, this.trustStorePassword.toCharArray());
            //This is defining the SSLContext so the trust store will be used
            //Getting default SSLContext to edit.
            SSLContext context = SSLContext.getInstance("SSL");
            //TrustMangers hold trust stores, more than one can be added
            TrustManagerFactory factory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //Adds the truststore to the factory
            factory.init(trustStore);
            //This is passed to the SSLContext init method
            TrustManager[] managers = factory.getTrustManagers();
            context.init(null, managers, null);
            //Sets our new SSLContext to be used.
            SSLContext.setDefault(context);
            return context;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException
            | CertificateException | KeyManagementException ex) {
            //Handle error
            log.error(ex);
            return null;
        }
    }

    @Bean
    @Primary
    @LoadBalanced
    public RestOperations restTemplate(RestTemplateBuilder rtb) {
        RestTemplate restTemplate = rtb.build();
        if (CloudPlatform.HEROKU.isActive(this.env)) {
            log.info("Heroku detected, rest template does not setup SSL");
        } else {
            log.info("Not Heroku, rest template is setting up SSL");
            HttpClient httpClient = HttpClients.custom().setSSLContext(this.customSSL()).build();
            ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(
                httpClient);
            restTemplate.setRequestFactory(requestFactory);
        }
        restTemplate.getInterceptors()
            .add(new BasicAuthenticationInterceptor(clientId, clientSecret));
        return restTemplate;
    }
}
