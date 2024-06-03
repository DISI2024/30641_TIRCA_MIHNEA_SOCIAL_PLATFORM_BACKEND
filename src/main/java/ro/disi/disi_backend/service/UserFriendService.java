package ro.disi.disi_backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.dto.UserFriendDTO;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.entity.UserFriend;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.model.enums.UserFriendStatus;
import ro.disi.disi_backend.repository.UserFriendRepository;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.repository.UserRepository;
import ro.disi.disi_backend.security.service.JwtService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserFriendService {

    private final UserFriendRepository userFriendRepository;

    private final UserProfileRepository userProfileRepository;

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public UserFriendService (UserFriendRepository userFriendRepository, UserProfileRepository userProfileRepository, JwtService jwtService, UserRepository userRepository) {
        this.userFriendRepository = userFriendRepository;
        this.userProfileRepository = userProfileRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

//    public UserFriend addFriendRequest (UserProfile user1, UserProfile user2) {
//
//        long requestedUserId = user1.getId();
//        long requestingUserId = user2.getId();
//
//        userFriendRepository.findById(requestingUserId).orElseThrow(()
//        -> new IllegalArgumentException("User not found"));
//
//        userFriendRepository.findById(requestedUserId).orElseThrow(()
//        -> new IllegalArgumentException("User not found"));
//
//        UserFriend userFriend = new UserFriend(requestingUserId, requestedUserId, UserFriendStatus.PENDING2);
//
//        return userFriendRepository.save(userFriend);
//
//    }

    public UserFriendDTO addFriendRequestByUserName (UserProfile user, String requestedUserName) {

        long requestingUserId = user.getId();

        userRepository.findById(requestingUserId).orElseThrow(()
                -> new IllegalArgumentException("User not found"));


        Optional<User> requestedUser = Optional.ofNullable(userRepository.findByUsername(requestedUserName).orElseThrow(()
                -> new IllegalArgumentException("User not found")));

        long requestedUserId = requestedUser.get().getId();
        UserProfile user2 = userProfileRepository.findByUserId(requestedUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userFriendRepository.findByUserProfile1AndUserProfile2(user,user2).isPresent() ||
                userFriendRepository.findByUserProfile1AndUserProfile2(user2, user).isPresent()) {
            throw new IllegalArgumentException("User friend already exists");
        }

        System.out.println("UserProfile1: " + user);
        System.out.println("UserProfile2: " + user2);

        UserFriend userFriend = new UserFriend();
        userFriend.setUserProfile1(user);
        userFriend.setUserProfile2(user2);
        userFriend.setStatus(UserFriendStatus.PENDING2);
        userFriendRepository.save(userFriend);
        UserFriend userFriend2 = new UserFriend();
        userFriend2.setUserProfile1(user2);
        userFriend2.setUserProfile2(user);
        userFriend2.setStatus(UserFriendStatus.PENDING1);
        userFriendRepository.save(userFriend);
        userFriendRepository.save(userFriend2);
        return new UserFriendDTO(user,user2, userFriend.getStatus());
    }

    public int removeFriend(UserProfile userProfile, String requestedUserName) {
        try {
            boolean found = false;
            found = userRepository.findByUsername(requestedUserName).isPresent();
            if (found == true) {
                User requestedUser = userRepository.findByUsername(requestedUserName).orElseThrow();
                UserProfile userProfile2 = userProfileRepository.findByUserId(requestedUser.getId()).orElseThrow();
                System.out.println("UserProfile: " + userProfile);
                System.out.println("UserProfile2: " + userProfile2);
                UserFriend userFriendToDelete = userFriendRepository.findByUserProfile1AndUserProfile2(userProfile, userProfile2)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                UserFriend userFriendToDelete2 = userFriendRepository.findByUserProfile1AndUserProfile2(userProfile2, userProfile)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));
                userFriendRepository.delete(userFriendToDelete);
                userFriendRepository.delete(userFriendToDelete2);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("User not found or user is not a friend");
        }
        return 0;
    }

    public List<UserFriendDTO> getAllFriends(UserProfile userProfile) {
        List<UserFriend> friends = new ArrayList<>();
        friends = userFriendRepository.getAllByUserProfile1(userProfile);
        for (UserFriend userFriend : friends) {
            System.out.println(userFriend);
        }
        List<UserFriendDTO>friendsDTO = new ArrayList<>();
        for (UserFriend userFriend : friends) {
            friendsDTO.add(new UserFriendDTO(userFriend.getUserProfile1(), userFriend.getUserProfile2(), userFriend.getStatus()));
        }
        return friendsDTO;
    }

}
