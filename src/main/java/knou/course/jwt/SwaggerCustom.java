package knou.course.jwt;

import java.util.Optional;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.stereotype.Component;

@Component
public class SwaggerCustom {

    @Bean
    OpenApiCustomizer springSecurityLoginEndpointCustomiser(ApplicationContext applicationContext) {
        FilterChainProxy filterChainProxy = applicationContext.getBean(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, FilterChainProxy.class);
        return openAPI -> {
            for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {
                Optional<UsernamePasswordAuthenticationFilter> optionalFilter =
                        filterChain.getFilters().stream()
                                .filter(UsernamePasswordAuthenticationFilter.class::isInstance)
                                .map(UsernamePasswordAuthenticationFilter.class::cast)
                                .findAny();
                if (optionalFilter.isPresent()) {
                    UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter = optionalFilter.get();
                    Operation operation = new Operation();
                    Schema<?> schema = new ObjectSchema()
                            .addProperties(usernamePasswordAuthenticationFilter.getUsernameParameter(), new StringSchema())
                            .addProperties(usernamePasswordAuthenticationFilter.getPasswordParameter(), new StringSchema());
                    RequestBody requestBody = new RequestBody().content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE, new MediaType().schema(schema)));
                    operation.requestBody(requestBody);
                    ApiResponses apiResponses = new ApiResponses();
                    apiResponses.addApiResponse(String.valueOf(HttpStatus.OK.value()),
                            new ApiResponse().description(HttpStatus.OK.getReasonPhrase())
                                    .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                            new MediaType().example("{\"token\":\"sample-jwt-token\"}"))));

                    apiResponses.addApiResponse(String.valueOf(HttpStatus.FORBIDDEN.value()),
                            new ApiResponse().description(HttpStatus.FORBIDDEN.getReasonPhrase())
                                    .content(new Content().addMediaType(org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                            new MediaType().example("{\"error\":\"Forbidden\"}"))));

                    operation.responses(apiResponses);
                    operation.addTagsItem("user-controller");

                    PathItem pathItem = new PathItem().post(operation);
                    openAPI.getPaths().addPathItem("/api/v1/users/sign-in", pathItem);
                }
            }
        };
    }
}
