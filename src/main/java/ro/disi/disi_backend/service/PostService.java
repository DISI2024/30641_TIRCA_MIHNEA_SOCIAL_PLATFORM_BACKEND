package ro.disi.disi_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.Dto.PostDto;
import ro.disi.disi_backend.model.entity.Post;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.repository.PostRepository;
import ro.disi.disi_backend.repository.UserProfileRepository;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserProfileRepository userProfileRepository) {
        this.postRepository = postRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public Boolean createPost(PostDto postDto, UserProfile profile) {
        Post post = new Post(postDto.description(), postDto.image(), profile);
        List<Post> currentPosts = profile.getPosts();

        if (currentPosts.add(post)) {
            profile.setPosts(currentPosts);
            return true;
        }

        return false;
    }
}
