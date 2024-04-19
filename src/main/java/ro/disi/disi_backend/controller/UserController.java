package ro.disi.disi_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.disi.disi_backend.model.entity.UserProfile;
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
}
