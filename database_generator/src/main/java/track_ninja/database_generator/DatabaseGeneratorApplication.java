package track_ninja.database_generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import track_ninja.database_generator.exeption_handlers.RestTemplateResponseErrorHandler;
import track_ninja.database_generator.services.DatabaseGeneratorService;

@SpringBootApplication
public class DatabaseGeneratorApplication {

    private DatabaseGeneratorService service;
    private RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

    @Autowired
    public DatabaseGeneratorApplication(DatabaseGeneratorService service, RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        this.service = service;
        this.restTemplateResponseErrorHandler = restTemplateResponseErrorHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(DatabaseGeneratorApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.errorHandler(restTemplateResponseErrorHandler).build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate){
        return args -> {
            service.saveGenres(restTemplate);
            service.getTracks(restTemplate);
        };
    }


}
