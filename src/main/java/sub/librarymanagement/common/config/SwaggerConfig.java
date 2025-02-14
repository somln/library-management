package sub.librarymanagement.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityRequirement securityRequirement = new SecurityRequirement().addList("BearerAuth");

        return new OpenAPI()
                .info(new Info().title("API")
                        .description("Application API Documentation")
                        .version("v1.0"))
                .addSecurityItem(securityRequirement)
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("BearerAuth", securityScheme));
    }

    @Bean
    public OperationCustomizer addMethodNameToSummary() {
        return (operation, handlerMethod) -> {
            String methodName = handlerMethod.getMethod().getName();
            if (operation.getSummary() == null || operation.getSummary().isEmpty()) {
                operation.setSummary(methodName);
            } else {
                operation.setSummary(operation.getSummary() + " (" + methodName + ")");
            }
            return operation;
        };
    }

}