package ro.disi.disi_backend.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.disi.disi_backend.Model.User.UserProfile;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
}
