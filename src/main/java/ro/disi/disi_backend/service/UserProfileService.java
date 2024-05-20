package ro.disi.disi_backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.repository.UserRepository;
import ro.disi.disi_backend.security.service.JwtService;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Autowired
    public UserProfileService(UserProfileRepository userProfileRepository, JwtService jwtService, UserRepository userRepository) {
        this.userProfileRepository = userProfileRepository;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    public Long getUserProfileIdByUserId(String bearerToken) {
        String username = jwtService.extractUsernameFromBearerToken(bearerToken);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username));

        UserProfile profile = userProfileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(String.valueOf(user.getId())));

        return profile.getId();
    }
}
