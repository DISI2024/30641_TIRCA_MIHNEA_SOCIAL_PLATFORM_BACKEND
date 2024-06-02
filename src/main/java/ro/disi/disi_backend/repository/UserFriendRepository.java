package ro.disi.disi_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ro.disi.disi_backend.model.entity.UserFriend;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

    Optional<UserFriend> findById(long id);
    Optional<UserFriend> findByUserId1(long userId1);
    Optional<UserFriend> findByUserId2(long userId2);
    Optional<UserFriend> findByUserId1AndAndUserId2(long userId1, long userId2);
    List<UserFriend> getAllByUserId1Is(long userId1);
}
