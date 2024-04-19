package ro.disi.disi_backend.Service.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.Dto.User.AuthRequestDto;
import ro.disi.disi_backend.Dto.User.AuthResponseDto;
import ro.disi.disi_backend.Dto.User.NewUserDto;
import ro.disi.disi_backend.Model.User.User;
import ro.disi.disi_backend.Model.User.UserProfile;
import ro.disi.disi_backend.Repository.User.UserProfileRepository;
import ro.disi.disi_backend.Repository.User.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

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

    public AuthResponseDto processDummyAuthRequest(AuthRequestDto authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername()).orElse(null);
        if (user == null)
            return null;

        UserProfile userProfile = userProfileRepository.findByUser(user).orElse(null);
        if (userProfile == null)
            return null;

        if (user.getPassword().equals(authRequest.getPassword())) {
            AuthResponseDto authResponse = new AuthResponseDto(user.getId(), userProfile.getId());
            return authResponse;
        }

        return null;
    }
}
