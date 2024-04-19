package ro.disi.disi_backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.disi.disi_backend.Model.Message;
import ro.disi.disi_backend.Model.UserProfile;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<List<Message>> findAllBySenderUserProfileAndReceiverUserProfile(@Param("senderUserProfile") UserProfile senderUserProfile,
                                                                             @Param("receiverUserProfile") UserProfile receiverUserProfile);

    Optional<Message> findFirstBySenderUserProfileAndReceiverUserProfileOrderByIdDesc(
            @Param("senderUserProfile") UserProfile senderUserProfile,
            @Param("receiverUserProfile") UserProfile receiverUserProfile);
}
