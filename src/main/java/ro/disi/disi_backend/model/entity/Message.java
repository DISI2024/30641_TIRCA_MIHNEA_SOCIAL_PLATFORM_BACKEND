package ro.disi.disi_backend.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_user_profile_id", referencedColumnName = "id")
    private UserProfile senderUserProfile;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_user_profile_id", referencedColumnName = "id")
    private UserProfile receiverUserProfile;

    private boolean isSeenByReceiver;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    public Message(String content, UserProfile senderUserProfile, UserProfile receiverUserProfile) {
        this.content = content;
        this.senderUserProfile = senderUserProfile;
        this.receiverUserProfile = receiverUserProfile;
        this.isSeenByReceiver = false;
    }
}
