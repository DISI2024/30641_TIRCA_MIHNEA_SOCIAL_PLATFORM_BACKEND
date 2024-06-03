package ro.disi.disi_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.disi.disi_backend.model.entity.Post;
import ro.disi.disi_backend.model.enums.UserFriendStatus;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByUserProfile_IdInOrderByIdDesc(List<Long> userProfileIds);
}