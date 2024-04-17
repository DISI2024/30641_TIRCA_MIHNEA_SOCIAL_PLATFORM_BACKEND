package ro.disi.disi_backend.Controller.User;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.disi.disi_backend.Dto.User.NewUserDto;
import ro.disi.disi_backend.Model.User.UserProfile;
import ro.disi.disi_backend.Service.User.UserService;
import ro.disi.disi_backend.Utility.JsonUtility;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private final UserService userService;

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
