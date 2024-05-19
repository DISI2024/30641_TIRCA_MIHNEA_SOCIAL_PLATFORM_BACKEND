package ro.disi.disi_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.disi.disi_backend.Dto.PostDto;
import ro.disi.disi_backend.service.PostService;
import ro.disi.disi_backend.service.UserService;
import ro.disi.disi_backend.service.UserProfileService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final UserProfileService userProfileService;
    private final UserService userService;

    @Autowired
    public PostController(PostService postService, UserProfileService userProfileService, UserService userService) {
        this.postService = postService;
        this.userProfileService = userProfileService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createPost(@RequestBody PostDto postDto, @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok(postService.createPost(postDto, userProfileService.getUserProfileIdByUserId(token)));
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @DeleteMapping("/deleteClient/{id}")
    public ResponseEntity<Void> deletePostByClient(@PathVariable Long id, @RequestHeader(name = "Authorization") String token) {
        postService.deletePostByClient(id, userService.getUserData(token).username());
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseEntity<Void> deletePostByAdmin(@PathVariable Long id) {
        postService.deletePostByAdmin(id);
        return ResponseEntity.noContent().build();
    }
}
