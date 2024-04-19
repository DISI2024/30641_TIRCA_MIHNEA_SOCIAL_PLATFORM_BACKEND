package ro.disi.disi_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.disi.disi_backend.dto.NewUserDto;
import ro.disi.disi_backend.model.UserProfile;
import ro.disi.disi_backend.service.UserService;
import ro.disi.disi_backend.utility.JsonUtility;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getAllUserProfiles")
    public ResponseEntity<String> getAllUserProfiles() throws JsonProcessingException {
        List<UserProfile> allUserProfiles = userService.processGetAllUserProfilesRequest();
        if (allUserProfiles == null)
            return ResponseEntity.badRequest().build();

        return JsonUtility.createJsonResponse(allUserProfiles);
    }

    @PostMapping("/createNewUserAndProfile")
    public ResponseEntity<String> createNewUserAndProfile(@RequestBody NewUserDto requestBody) {
        boolean status = userService.processNewUserRequest(requestBody);
        if (!status)
            return ResponseEntity.badRequest().body("User and profile creation failed!");

        return ResponseEntity.ok("User and profile created successfully!");

    }
}
