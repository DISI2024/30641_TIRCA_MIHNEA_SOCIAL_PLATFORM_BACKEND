package ro.disi.disi_backend.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.Dto.UserDataDto;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.model.enums.UserRole;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.repository.UserRepository;
import ro.disi.disi_backend.security.service.JwtService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final JwtService jwtService;


    @Autowired
    public UserService(UserRepository userRepository, UserProfileRepository userProfileRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.jwtService = jwtService;
    }

    public List<UserProfile> processGetAllUserProfilesRequest() {
        return userProfileRepository.findAll();
    }

    /**
     * @return The authenticated user's information based on his token
     */
    public UserDataDto getUserData(String bearerToken) {
        String username = jwtService.extractUsernameFromBearerToken(bearerToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username));

        if (user.getRole().equals(UserRole.CLIENT)) {
            UserProfile profile = userProfileRepository.findByUserId(user.getId())
                    .orElseThrow();

            return new UserDataDto(user.getId(), user.getUsername(), profile.getFirstName(), profile.getLastName(), user.getRole());
        }
        return new UserDataDto(user.getId(), user.getUsername(), "ADMIN", "ADMIN", user.getRole());
    }

    public UserDataDto getUserByUsername(String bearerToken) {
        String username = jwtService.extractUsernameFromBearerToken(bearerToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username));

        Optional<UserProfile> profile = userProfileRepository.findByUserId(user.getId());
        return new UserDataDto(user.getId(), user.getUsername(), profile.get().getFirstName(), profile.get().getLastName(), user.getRole());
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User not found!");
        }

        userRepository.deleteById(userId);
    }
}
