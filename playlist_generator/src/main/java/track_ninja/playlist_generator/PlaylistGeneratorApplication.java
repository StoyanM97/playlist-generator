package track_ninja.playlist_generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.client.RestTemplate;
import track_ninja.playlist_generator.exceptions.exeption_handlers.RestTemplateResponseErrorHandler;

@SpringBootApplication
@EnableJpaRepositories
public class PlaylistGeneratorApplication {

    private RestTemplateResponseErrorHandler restTemplateResponseErrorHandler;

    @Autowired
    public PlaylistGeneratorApplication(RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        this.restTemplateResponseErrorHandler = restTemplateResponseErrorHandler;
    }

    public static void main(String[] args) {
        SpringApplication.run(PlaylistGeneratorApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.errorHandler(restTemplateResponseErrorHandler).build();
    }

}
