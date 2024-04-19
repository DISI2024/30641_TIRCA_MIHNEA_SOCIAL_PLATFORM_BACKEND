package ro.disi.disi_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.dto.NewUserDto;
import ro.disi.disi_backend.model.User;
import ro.disi.disi_backend.model.UserProfile;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public List<UserProfile> processGetAllUserProfilesRequest() {
        return userProfileRepository.findAll();
    }

    public boolean processNewUserRequest(NewUserDto requestBody) {

        if (requestBody == null)
            return false;

        User user = new User(requestBody.getUsername(),
                requestBody.getPassword(),
                null);

        UserProfile userProfile = new UserProfile(requestBody.getLastName(),
                requestBody.getFirstName());

        user.setUserProfile(userProfile);
        userProfile.setUser(user);

        User savedUser = userRepository.save(user);
        UserProfile savedUserProfile = userProfileRepository.save(userProfile);
        return savedUser != null && savedUserProfile != null;
    }
}
