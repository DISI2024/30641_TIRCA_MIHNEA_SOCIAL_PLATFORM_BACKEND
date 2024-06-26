package ro.disi.disi_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.disi.disi_backend.dto.PostDto;
import ro.disi.disi_backend.model.entity.Post;
import ro.disi.disi_backend.model.entity.UserFriend;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.model.enums.UserFriendStatus;
import ro.disi.disi_backend.repository.PostRepository;
import ro.disi.disi_backend.repository.UserFriendRepository;
import ro.disi.disi_backend.repository.UserProfileRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserProfileRepository userProfileRepository;

    //am adaugat acm
    private final UserFriendRepository userFriendRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserProfileRepository userProfileRepository, UserFriendRepository userFriendRepository) {
        this.postRepository = postRepository;
        this.userProfileRepository = userProfileRepository;
        this.userFriendRepository = userFriendRepository;
    }

    @Transactional
    public void createPost(PostDto postDto, Long id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found!"));

        LocalDateTime timestamp = LocalDateTime.now();

        Post post = new Post(postDto.description(), postDto.image(), profile, timestamp);

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

//    public List<PostDto> getUserAndFriendsPosts(long userId) {
//        List<Post> userPosts = postRepository.findAllByUserProfile_Id(userId);
//        List<Post> friendsPosts = postRepository.findAllFriendsPosts(userId, UserFriendStatus.NORMAL);
//        List<Post> allPosts = new ArrayList<>(userPosts);
//        allPosts.addAll(friendsPosts);
//        return allPosts.stream().map(post -> new PostDto(post.getDescription(), post.getImage())).collect(Collectors.toList());
//    }


    //am schimbat acm
    @Transactional(readOnly = true)
    public List<Post> getPostsForUserAndFriends(Long userId) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserProfile not found for id: " + userId));

        // Fetch friends regardless of status using the new method
        List<UserFriend> friends = userFriendRepository.findAllByUserProfile1(userProfile);
        List<Long> userIds = new ArrayList<>();
        userIds.add(userId); // Include the user's own posts
        for (UserFriend friend : friends) {
            userIds.add(friend.getUserProfile2().getId());
        }

        System.out.println("User IDs for fetching posts: " + userIds);

        List<Post> posts = postRepository.findByUserProfile_IdInOrderByIdDesc(userIds);

        System.out.println("Fetched posts: " + posts.size());

        return posts;
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsForUser(Long userId) {
        UserProfile userProfile = userProfileRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("UserProfile not found for id: " + userId));

        List<Post> posts = postRepository.findByUserProfile_IdOrderByIdDesc(userProfile.getId());

        System.out.println("Fetched posts: " + posts.size());

        return posts;
    }

}

