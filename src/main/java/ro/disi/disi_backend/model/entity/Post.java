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
    private byte[] post;

    public Post(String description, byte[] post) {
        this.description = description;
        this.post = post;
    }
}
