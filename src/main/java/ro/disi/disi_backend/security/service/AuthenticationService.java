package ro.disi.disi_backend.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.model.enums.UserRole;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.repository.UserRepository;
import ro.disi.disi_backend.security.model.dto.AuthenticationRequest;
import ro.disi.disi_backend.security.model.dto.AuthenticationResponse;
import ro.disi.disi_backend.security.model.dto.RegisterRequest;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserProfileRepository userProfileRepository;

    private User generateUser(RegisterRequest request) {
        return User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.CLIENT)
                .build();
    }

    private UserProfile generateUserProfile(RegisterRequest request, User user) {
        return new UserProfile(user, request.firstName(), request.lastName());
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = generateUser(request);
        UserProfile userProfile = generateUserProfile(request, user);
        userRepository.save(user);
        userProfileRepository.save(userProfile);

        String jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String username = request.username();
        String password = request.password();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found!"));

        String jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }
}
