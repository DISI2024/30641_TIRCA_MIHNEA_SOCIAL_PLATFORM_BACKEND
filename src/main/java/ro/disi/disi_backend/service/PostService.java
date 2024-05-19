package ro.disi.disi_backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    public Boolean createPost(PostDto postDto, Long id) {
        UserProfile profile = userProfileRepository.findByUserId(id).orElseThrow(() -> new IllegalArgumentException("Profile not found!"));;
        Post post = new Post(postDto.description(), postDto.image(), profile);
        List<Post> currentPosts = profile.getPosts();

        if (currentPosts.add(post)) {
            profile.setPosts(currentPosts);
            userProfileRepository.save(profile);
            return true;
        }

        return false;
    }

    @Transactional
    public void deletePostByClient(Long postId, String username) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found!"));
        if (!post.getUserProfile().getUser().getUsername().equals(username)) {
            throw new SecurityException("You do not have permission to delete this post!");
        }
        postRepository.delete(post);
    }

    @Transactional
    public void deletePostByAdmin(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post not found!"));
        postRepository.delete(post);
    }
}
