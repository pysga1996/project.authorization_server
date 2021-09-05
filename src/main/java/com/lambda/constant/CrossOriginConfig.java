package com.lambda.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class CrossOriginConfig {

    public static final List<String> allowedOrigins = Collections.unmodifiableList(Arrays.asList(
        "https://alpha-sound.netlify.app",
        "https://kappa-talk.netlify.app",
        "https://omega-buy.netlify.app",
        "http://localhost:3000",
        "http://localhost:4200",
        "http://localhost:4300",
        "http://localhost.test:3000",
        "http://localhost.test:4200",
        "http://localhost.test:4300",
        "http://omega-buy-client:3000",
        "http://kappa-talk-client:4200",
        "http://alpha-sound-client:4300"
    ));

}
