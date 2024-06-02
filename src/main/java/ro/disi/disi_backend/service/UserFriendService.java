package ro.disi.disi_backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.Dto.UserFriendDTO;
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

    public UserFriend addFriendRequest (UserProfile user1, UserProfile user2) {

        long requestedUserId = user1.getId();
        long requestingUserId = user2.getId();

        userFriendRepository.findById(requestingUserId).orElseThrow(()
        -> new IllegalArgumentException("User not found"));

        userFriendRepository.findById(requestedUserId).orElseThrow(()
        -> new IllegalArgumentException("User not found"));

        UserFriend userFriend = new UserFriend(requestingUserId, requestedUserId, UserFriendStatus.PENDING2);

        return userFriendRepository.save(userFriend);

    }

    public UserFriendDTO addFriendRequestByUserName (UserProfile user, String requestedUserName) {

        long requestingUserId = user.getId();

        userRepository.findById(requestingUserId).orElseThrow(()
                -> new IllegalArgumentException("User not found"));


        Optional<User> requestedUser = Optional.ofNullable(userRepository.findByUsername(requestedUserName).orElseThrow(()
                -> new IllegalArgumentException("User not found")));

        long requestedUserId = requestedUser.get().getId();

        UserFriend userFriend = new UserFriend(requestingUserId, requestedUserId, UserFriendStatus.PENDING2);

        userFriendRepository.save(userFriend);
        return new UserFriendDTO(userFriend.getUser1().getId(), userFriend.getUser2().getId(), userFriend.getStatus());
    }

    public int removeFriend(long requesterId) {
        try {
            boolean found = false;
            found = userFriendRepository.findByUserId1(requesterId).isPresent();
            found = userFriendRepository.findByUserId2(requesterId).isPresent();
            if (found == true) {
                long removedId = userFriendRepository.findByUserId1(requesterId).get().getId();
                userFriendRepository.deleteById(removedId);
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("User not found");
        }
        return 0;
    }

    public List<UserFriendDTO> getAllFriends(UserProfile userProfile) {

        long requesterId = userProfile.getId();

        List<UserFriend> friends = new ArrayList<>();
        friends = userFriendRepository.getAllByUserId1Is(requesterId);

        List<UserFriendDTO>friendsDTO = new ArrayList<>();
        for (UserFriend userFriend : friends) {
            friendsDTO.add(new UserFriendDTO(userFriend.getUser1().getId(), userFriend.getUser2().getId(), userFriend.getStatus()));
        }
        return friendsDTO;
    }

}
