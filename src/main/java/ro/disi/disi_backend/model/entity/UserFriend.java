package ro.disi.disi_backend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ro.disi.disi_backend.model.enums.UserFriendStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_friends")
public class UserFriend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private UserProfile user1;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private UserProfile user2;

    @NotNull
    private UserFriendStatus status;

    public UserFriend(@NotNull long userId1, @NotNull long userId2, @NotNull UserFriendStatus status) {}

}
