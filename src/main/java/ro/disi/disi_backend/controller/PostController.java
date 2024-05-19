package ro.disi.disi_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.disi.disi_backend.Dto.PostDto;
import ro.disi.disi_backend.Service.PostService;
import ro.disi.disi_backend.Service.UserProfileService;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;
    private final UserProfileService userProfileService;

    @Autowired
    public PostController(PostService postService, UserProfileService userProfileService) {
        this.postService = postService;
        this.userProfileService = userProfileService;
    }

    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @PostMapping("/create")
    public ResponseEntity<Boolean> createPost(@RequestBody PostDto postDto, @RequestHeader(name = "Authorization") String token) {
        return ResponseEntity.ok(postService.createPost(postDto, userProfileService.getUserProfileIdByUserId(token)));
    }
}
