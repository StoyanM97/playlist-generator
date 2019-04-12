package track_ninja.database_generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import track_ninja.database_generator.services.DatabaseGeneratorService;


@SpringBootApplication
public class DatabaseGeneratorApplication {

    private DatabaseGeneratorService service;

    @Autowired
    public DatabaseGeneratorApplication(DatabaseGeneratorService service) {
        this.service = service;
    }

    public static void main(String[] args) {
        SpringApplication.run(DatabaseGeneratorApplication.class, args);
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate) throws Exception {

        return args -> {
            service.saveGenres(restTemplate);
            service.saveTracks(restTemplate);
        };
    }


}
