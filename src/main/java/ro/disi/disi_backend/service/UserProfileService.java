package ro.disi.disi_backend.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.repository.UserRepository;
import ro.disi.disi_backend.security.service.JwtService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final String uploadDir = "path/to/upload/dir";

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
    public UserProfile createUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public UserProfile updateUserProfile(Long id, UserProfile userProfile) {
        UserProfile existingProfile = userProfileRepository.findById(id).orElseThrow();
        existingProfile.setFirstName(userProfile.getFirstName());
        existingProfile.setLastName(userProfile.getLastName());
        existingProfile.setDescription(userProfile.getDescription());
        // Update other fields as necessary
        return userProfileRepository.save(existingProfile);
    }

    public UserProfile updateDescription(Long id, String description) {
        UserProfile userProfile = userProfileRepository.findByUserId(id)
                .orElseThrow(() -> new EntityNotFoundException("UserProfile not found for id: " + id));
        userProfile.setDescription(description);
        return userProfileRepository.save(userProfile);
    }

    public UserProfile updateProfilePicture(Long id, MultipartFile file) throws IOException {
        UserProfile existingProfile = userProfileRepository.findById(id).orElseThrow();
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.write(filePath, file.getBytes());
        existingProfile.setProfilePictureUrl(filePath.toString());
        return userProfileRepository.save(existingProfile);
    }
    public UserProfile getUserProfileByUserId(Long userId) {
        System.out.println("userId: " + userId);
        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("UserProfile not found for userId: " + userId));
        return profile;
    }

    public UserProfile getUserProfileByUserIdOnly(long userId) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(Long.toString(userId)));

        return userProfile;
    }

}
