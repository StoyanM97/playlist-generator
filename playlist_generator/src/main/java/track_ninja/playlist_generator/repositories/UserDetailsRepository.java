package track_ninja.playlist_generator.repositories;

import org.springframework.data.repository.CrudRepository;
import track_ninja.playlist_generator.models.UserDetailsModel;

public interface UserDetailsRepository extends CrudRepository<UserDetailsModel, Integer> {
    UserDetailsModel findByIsDeletedFalseAndUser_Username(String username);
}
