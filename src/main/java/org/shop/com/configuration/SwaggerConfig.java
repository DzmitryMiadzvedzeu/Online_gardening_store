package org.shop.com.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Garden Shop Online API",
                version = "1.0.0",
                description = "API for Garden Shop Online platform operations",
                contact = @Contact(
                        name = "SpringVibes",
                        email = "garden@onlineshop.com",
                        url = "http://www.gardenOnlineShop.com"
                )
        )
)
public class SwaggerConfig {

    @Bean
    @Primary
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .packagesToScan("org.shop.com.controller")
                .build();
    }

    private GroupedOpenApi groupOpenApi(String groupName, String paths) {
        return GroupedOpenApi.builder()
                .group(groupName)
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi cartApi() {
        return groupOpenApi("carts", "/v1/carts/**");
    }

    @Bean
    public GroupedOpenApi categoryApi() {
        return groupOpenApi("categories", "/v1/categories/**");
    }

    @Bean
    public GroupedOpenApi favoritesApi() {
        return groupOpenApi("favorites", "/v1/favorites/**");
    }

    @Bean
    public GroupedOpenApi ordersApi() {
        return groupOpenApi("orders", "/v1/orders/**");
    }

    @Bean
    public GroupedOpenApi productsApi() {
        return groupOpenApi("products", "/v1/products/**");
    }

    @Bean
    public GroupedOpenApi usersApi() {
        return groupOpenApi("users", "/v1/users/**");
    }
}