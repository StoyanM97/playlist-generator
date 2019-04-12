package track_ninja.playlist_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class PlaylistGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaylistGeneratorApplication.class, args);
    }

}
