package com.lambda.config.custom;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Log4j2
@Configuration
public class SSLConfig {

    @Value("classpath:vengeance.jks")
    private Resource trustStore;

    @Value("${custom.trust-store-password}")
    private String trustStorePassword;

    @Value("${custom.trust-store-type}")
    private String trustStoreType;

//    @PostConstruct
//    private void configureSSL() throws IOException {
////        System.setProperty("javax.net.debug", "ssl");
//        System.setProperty("https.protocols", "TLSv1.2");
//        System.setProperty("javax.net.ssl.trustStore", Paths.get(trustStore.getURI()).toString());
//        System.setProperty("javax.net.ssl.trustStorePassword", Objects.requireNonNull(trustStorePassword));
//        System.setProperty("javax.net.ssl.trustStoreType", Objects.requireNonNull(trustStoreType));
//    }

    @PostConstruct
    public void configureSSL() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, sslSession) -> hostname.equals("localhost"));
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
            TrustManagerFactory factory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            //Adds the truststore to the factory
            factory.init(trustStore);
            //This is passed to the SSLContext init method
            TrustManager[] managers = factory.getTrustManagers();
            context.init(null, managers, null);
            //Sets our new SSLContext to be used.
            SSLContext.setDefault(context);
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException
                | CertificateException | KeyManagementException ex) {
            //Handle error
            log.error(ex);
        }
    }
}
