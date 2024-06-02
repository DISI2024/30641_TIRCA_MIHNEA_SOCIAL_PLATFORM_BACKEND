package ro.disi.disi_backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ro.disi.disi_backend.Dto.UserDataDto;
import ro.disi.disi_backend.Dto.UserFriendDTO;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.service.UserFriendService;
import ro.disi.disi_backend.service.UserProfileService;
import ro.disi.disi_backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("api/userFriend")
public class UserFriendController {
    private final UserFriendService userFriendService;
    private final UserService userService;
    private final UserProfileService userProfileService;

    @Autowired
    public UserFriendController(UserFriendService userFriendService, UserService userService, UserProfileService userProfileService) {this.userFriendService = userFriendService;
        this.userService = userService;
        this.userProfileService = userProfileService;
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
    @GetMapping("/createFriendRequest")
    public UserFriendDTO createFriendRequest(@RequestHeader(name = "Authorization") String token, String requestedUsername) {

        UserDataDto userData1 = userService.getUserByUsername(token);

        UserProfile userProfile1 = userProfileService.getUserProfileByUserIdOnly(userData1.id());


        return userFriendService.addFriendRequestByUserName(userProfile1, requestedUsername);
    }

    @GetMapping("/getAllFriends")
    public ResponseEntity<String> getAllFriends(@RequestHeader(name = "Authorization") String token) throws JsonProcessingException {
        System.out.println("O intrat aci");
        UserDataDto userData = userService.getUserByUsername(token);
        //long id = userData.id();
        //long id = 1;
        UserProfile userProfile = userProfileService.getUserProfileByUserIdOnly(userData.id());
        List<UserFriendDTO> friends = userFriendService.getAllFriends(userProfile);

        if (friends.size() > 0) {
            return ResponseEntity.ok().body(friends.toString());
        } else {
            System.out.println("Ii bai");
            return ResponseEntity.badRequest().body("No friends found");
        }
    }

//    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_ADMIN')")
//    @GetMapping("/removeFriend")
//    public UserFriendDTO removeFriend(@RequestHeader(name = "Authorization") String token, String username) {
//
//        UserDataDto userData = userService.getUserByUsername(token);
//        long id1 = userData.id();
//        return userFriendService.addFriendRequestByUserName(id1, username);
//    }

}
