package ro.disi.disi_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.disi.disi_backend.model.entity.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
