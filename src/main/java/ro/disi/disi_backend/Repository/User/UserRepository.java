package ro.disi.disi_backend.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.disi.disi_backend.Model.User.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
