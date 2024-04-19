package ro.disi.disi_backend.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.disi.disi_backend.Model.User.User;
import ro.disi.disi_backend.Model.User.UserProfile;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    Optional<UserProfile> findByUser(@Param("user") User user);
}
