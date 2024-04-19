package ro.disi.disi_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.disi.disi_backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
