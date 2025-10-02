package bank.api.infra.springDoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.web.method.HandlerMethod;

@Configuration
public class SpringDocConfigurations {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .info(new Info()
                        .title("Bank API")
                        .description("API RESTful desenvolvida com **Java + Spring Boot**, que simula operações bancárias básicas, como: transferências, pix, depósitos, saques")
                        .contact(new Contact()
                                .name("João Jales")
                                .email("joaoricardocirino@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://bank/api/licenca")));
    }


    @Bean
    public OperationCustomizer customizePageable() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            // Verifica se o mét0do do controller usa Pageable como parâmetro
            if (handlerMethod.getMethodParameters().length > 0 &&
                    handlerMethod.getMethodParameters()[0].getParameterType().equals(Pageable.class)) {

                // 1. Remove o parâmetro Pageable do corpo da requisição (Body)
                operation.getParameters().removeIf(p -> p.getName().equals("pageable"));

                // 2. Adiciona o parâmetro 'page' como Query Parameter
                operation.addParametersItem(new Parameter()
                        .in("query")
                        .name("page")
                        .description("Número da página (inicia em 0)")
                        .schema(new StringSchema()._default("0")));

                // 3. Adiciona o parâmetro 'size' como Query Parameter
                operation.addParametersItem(new Parameter()
                        .in("query")
                        .name("size")
                        .description("Tamanho da página")
                        .schema(new StringSchema()._default("10")));

                // 4. Adiciona o parâmetro 'sort' como Query Parameter
                operation.addParametersItem(new Parameter()
                        .in("query")
                        .name("sort")
                        .description("Critério de ordenação no formato: campo,(asc|desc)")
                        .schema(new StringSchema()._default("")));
            }
            return operation;
        };
    }
}
