package ro.disi.disi_backend.Model.Chat;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import ro.disi.disi_backend.Model.User.UserProfile;

import java.sql.Timestamp;

@Entity
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

    public Message() {

    }

    public Message(String content, UserProfile senderUserProfile, UserProfile receiverUserProfile) {
        this.content = content;
        this.senderUserProfile = senderUserProfile;
        this.receiverUserProfile = receiverUserProfile;
        this.isSeenByReceiver = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserProfile getSenderUserProfile() {
        return senderUserProfile;
    }

    public void setSenderUserProfile(UserProfile senderUserProfile) {
        this.senderUserProfile = senderUserProfile;
    }

    public UserProfile getReceiverUserProfile() {
        return receiverUserProfile;
    }

    public void setReceiverUserProfile(UserProfile receiverUserProfile) {
        this.receiverUserProfile = receiverUserProfile;
    }

    public boolean isSeenByReceiver() {
        return isSeenByReceiver;
    }

    public void setSeenByReceiver(boolean seenByReceiver) {
        isSeenByReceiver = seenByReceiver;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
