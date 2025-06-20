package star;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "star",
        "star.controller"
})
@OpenAPIDefinition
public class StarApplication {
    public static void main(String[] args){
        SpringApplication.run(StarApplication.class);
    }
}
