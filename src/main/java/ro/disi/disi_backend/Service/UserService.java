package ro.disi.disi_backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.Dto.NewUserDto;
import ro.disi.disi_backend.Model.User;
import ro.disi.disi_backend.Model.UserProfile;
import ro.disi.disi_backend.Repository.UserProfileRepository;
import ro.disi.disi_backend.Repository.UserRepository;

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
