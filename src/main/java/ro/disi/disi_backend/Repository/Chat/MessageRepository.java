package ro.disi.disi_backend.Repository.Chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ro.disi.disi_backend.Model.Chat.Message;
import ro.disi.disi_backend.Model.User.UserProfile;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<List<Message>> findAllBySenderUserProfileAndReceiverUserProfile(@Param("senderUserProfile") UserProfile senderUserProfile,
                                                                             @Param("receiverUserProfile") UserProfile receiverUserProfile);

    Optional<Message> findFirstBySenderUserProfileAndReceiverUserProfileOrderByIdDesc(
            @Param("senderUserProfile") UserProfile senderUserProfile,
            @Param("receiverUserProfile") UserProfile receiverUserProfile);
}
