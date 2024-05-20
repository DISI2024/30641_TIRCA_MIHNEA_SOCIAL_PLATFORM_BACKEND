package ro.disi.disi_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ro.disi.disi_backend.model.entity.Post;
import ro.disi.disi_backend.model.enums.UserFriendStatus;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUserProfile_Id(long userProfileId);

    @Query("SELECT p FROM Post p WHERE p.userProfile.id IN " +
            "(SELECT CASE WHEN uf.userId1 = :userId THEN uf.userId2 ELSE uf.userId1 END " +
            "FROM UserFriend uf WHERE (uf.userId1 = :userId OR uf.userId2 = :userId) AND uf.status = :status)")
    List<Post> findAllFriendsPosts(long userId, UserFriendStatus status);
}