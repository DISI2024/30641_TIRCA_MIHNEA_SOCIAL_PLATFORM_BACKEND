package ro.disi.disi_backend.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.disi.disi_backend.Model.User.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(@Param("username") String username);
}
