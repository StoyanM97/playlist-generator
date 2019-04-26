package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.models.UserDetails;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Integer> {
    UserDetails findByIsDeletedFalseAndUser_Username(String username);
}
