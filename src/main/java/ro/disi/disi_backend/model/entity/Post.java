package ro.disi.disi_backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    public Post(String description, byte[] image, UserProfile userProfile) {
        this.description = description;
        this.image = image;
        this.userProfile = userProfile;
    }
}
