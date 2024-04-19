package ro.disi.disi_backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.disi.disi_backend.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
