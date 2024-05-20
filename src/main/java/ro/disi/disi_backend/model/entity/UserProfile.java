package ro.disi.disi_backend.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profiles")
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String lastName;

    @NotNull
    private String firstName;

    private String description;

    private String profilePictureURL;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Post> posts = new ArrayList<>();

//    @ManyToMany
//    @JoinTable(
//            name = "user_friends",
//            joinColumns = @JoinColumn(name = "user_profile_id"),
//            inverseJoinColumns = @JoinColumn(name = "friend_id")
//    )
//    @JsonManagedReference
//    private List<UserProfile> friends = new ArrayList<>();
//
//    @ManyToMany(mappedBy = "friends")
//    @JsonBackReference
//    private List<UserProfile> friendOf = new ArrayList<>();



    public UserProfile(User user, String firstName, String lastName) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePictureURL = "https://imgur.com/ZQPyimj";
    }

    public void setProfilePictureUrl(String toString) {
    }
}
