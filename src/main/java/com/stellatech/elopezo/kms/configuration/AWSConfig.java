package com.stellatech.elopezo.kms.configuration;

import com.amazonaws.encryptionsdk.AwsCrypto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;

@Configuration
public class AWSConfig {

   @Value("${aws.region}")
    private String region;

    @Bean
    public KmsClient kmsClient() {
        Region region = Region.of(this.region);

        return KmsClient.builder()
                .region(region)
                .build();

    }


}
