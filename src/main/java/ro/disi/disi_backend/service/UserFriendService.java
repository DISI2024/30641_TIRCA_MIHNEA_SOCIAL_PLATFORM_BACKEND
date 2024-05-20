package ro.disi.disi_backend.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.entity.UserFriend;
import ro.disi.disi_backend.model.enums.UserFriendStatus;
import ro.disi.disi_backend.repository.UserFriendRepository;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.repository.UserRepository;
import ro.disi.disi_backend.security.service.JwtService;

@Service
@Transactional
public class UserFriendService {

    private final UserFriendRepository userFriendRepository;

    private final UserProfileRepository userProfileRepository;

    private final JwtService jwtService;

    @Autowired
    public UserFriendService (UserFriendRepository userFriendRepository, UserProfileRepository userProfileRepository, JwtService jwtService) {
        this.userFriendRepository = userFriendRepository;
        this.userProfileRepository = userProfileRepository;
        this.jwtService = jwtService;
    }

    public UserFriend addFriendRequest (long requestingUserId, long requestedUserId) {

        userFriendRepository.findById(requestingUserId).orElseThrow(()
        -> new IllegalArgumentException("User not found"));

        userFriendRepository.findById(requestedUserId).orElseThrow(()
        -> new IllegalArgumentException("User not found"));

        UserFriend userFriend = new UserFriend(requestingUserId, requestedUserId, UserFriendStatus.PENDING2);

        return userFriendRepository.save(userFriend);

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

}
