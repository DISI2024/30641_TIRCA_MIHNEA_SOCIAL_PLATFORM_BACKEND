package ro.disi.disi_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.disi.disi_backend.dto.PostDto;
import ro.disi.disi_backend.model.entity.Post;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.model.enums.UserFriendStatus;
import ro.disi.disi_backend.repository.PostRepository;
import ro.disi.disi_backend.repository.UserProfileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserProfileRepository userProfileRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserProfileRepository userProfileRepository) {
        this.postRepository = postRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public void createPost(PostDto postDto, Long id) {
        UserProfile profile = userProfileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Profile not found!"));
        ;
        Post post = new Post(postDto.description(), postDto.image(), profile);
        List<Post> currentPosts = profile.getPosts();

        currentPosts.add(0, post);
        profile.setPosts(currentPosts);
        userProfileRepository.save(profile);
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

    public List<PostDto> getUserAndFriendsPosts(long userId) {
        List<Post> userPosts = postRepository.findAllByUserProfile_Id(userId);
        List<Post> friendsPosts = postRepository.findAllFriendsPosts(userId, UserFriendStatus.NORMAL);
        List<Post> allPosts = new ArrayList<>(userPosts);
        allPosts.addAll(friendsPosts);
        return allPosts.stream().map(post -> new PostDto(post.getDescription(), post.getImage())).collect(Collectors.toList());
    }
}

