package com.kanezi.springdoc_openapi_showcase;

import io.swagger.v3.oas.models.info.Info;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;

@Configuration
@Data
@NoArgsConstructor
public class OpenApiConfig {

    // tag::openapi-groups[]
    @Value("${springdoc.version}")
    String springDocVersion;

    @Bean
    GroupedOpenApi actuatorApi(WebEndpointProperties endpointProperties) {
        return GroupedOpenApi
                .builder()
                .addOpenApiCustomizer(apiCustomizer -> apiCustomizer.info(new Info().version(springDocVersion).description("Actuator API")))
                .group("actuator")
                .pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
                .build();
    }

    @Bean
    GroupedOpenApi emailApi() {
        return GroupedOpenApi
                .builder()
                .addOpenApiCustomizer(apiCustomizer -> apiCustomizer.info(new Info().description("Email API")))
                .group("email")
                .packagesToScan("com.kanezi.springdoc_openapi_showcase")
                .build();
    }
    // end::openapi-groups[]

}
