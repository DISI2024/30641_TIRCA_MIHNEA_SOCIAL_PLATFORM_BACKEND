package ro.disi.disi_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.service.UserProfileService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/user-profiles")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @PostMapping
    public ResponseEntity<UserProfile> createUserProfile(@RequestBody UserProfile userProfile) {
        UserProfile createdProfile = userProfileService.createUserProfile(userProfile);
        return ResponseEntity.ok(createdProfile);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UserProfile userProfile) {
        System.out.println(userProfile);
        UserProfile updatedProfile = userProfileService.updateUserProfile(id, userProfile);
        return ResponseEntity.ok(updatedProfile);
    }

    @PutMapping("/{id}/profile-picture")
    public ResponseEntity<UserProfile> updateProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        try {
            UserProfile updatedProfile = userProfileService.updateProfilePicture(id, file);
            return ResponseEntity.ok(updatedProfile);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}/description")
    public ResponseEntity<UserProfile> updateDescription(
            @PathVariable Long id,
            @RequestBody String description) {
        UserProfile updatedProfile = userProfileService.updateDescription(id, description);
        return ResponseEntity.ok(updatedProfile);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserProfileByUserId(@PathVariable Long id) {
        System.out.println("UserId in controller: " + id);
        UserProfile userProfile = userProfileService.getUserProfileByUserId(id);
        return ResponseEntity.ok(userProfile);
    }
//
//    @GetMapping
//    public ResponseEntity<List<UserProfile>> getAllUserProfiles() {
//        List<UserProfile> userProfiles = userProfileService.getAllUserProfiles();
//        return ResponseEntity.ok(userProfiles);
//    }
}
