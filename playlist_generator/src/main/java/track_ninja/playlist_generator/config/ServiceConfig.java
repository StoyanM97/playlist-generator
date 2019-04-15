package track_ninja.playlist_generator.config;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.context.annotation.Bean;
import track_ninja.playlist_generator.models.*;

import javax.jws.soap.SOAPBinding;

public class ServiceConfig {
    @Bean
    public SessionFactory createSessionFactory() {
        System.out.println("SessionFactory was created.");

        return new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Album.class)
                .addAnnotatedClass(Artist.class)
                .addAnnotatedClass(Authority.class)
                .addAnnotatedClass(Genre.class)
                .addAnnotatedClass(Playlist.class)
                .addAnnotatedClass(Track.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();
    }
}
