package ro.disi.disi_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ro.disi.disi_backend.model.entity.UserFriend;
import ro.disi.disi_backend.model.entity.UserProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFriendRepository extends JpaRepository<UserFriend, Long> {

    Optional<UserFriend> findById(long id);
    Optional<UserFriend> findByUserProfile1(UserProfile profile);
    Optional<UserFriend> findByUserProfile2(UserProfile profile);
    List<UserFriend> getAllByUserProfile1(UserProfile profile);
    List<UserFriend> getAllByUserProfile2(UserProfile profile);
    Optional<UserFriend>findByUserProfile1AndUserProfile2(UserProfile profile1, UserProfile profile2);
}
