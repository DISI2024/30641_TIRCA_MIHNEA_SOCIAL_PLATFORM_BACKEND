package ro.disi.disi_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.entity.UserProfile;
import ro.disi.disi_backend.model.enums.UserRole;
import ro.disi.disi_backend.repository.UserProfileRepository;
import ro.disi.disi_backend.repository.UserRepository;

import java.util.Optional;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final ro.disi.disi_backend.repository.UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CommandLineAppStartupRunner(UserRepository userRepository, UserProfileRepository userProfileRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Optional<User> optionalAdmin = userRepository.findByUsername("admin");
        if (optionalAdmin.isEmpty()) {
            User admin = User.builder()
                    .email("admin.disi@gmail.ro")
                    .username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(UserRole.ADMIN)
                    .build();

            UserProfile adminUserProfile = new UserProfile(admin, "Florinel", "Coman");
            admin.setUserProfile(adminUserProfile);

            userRepository.save(admin);
            userProfileRepository.save(adminUserProfile);
        }

        Optional<User> optionalClient1 = userRepository.findByUsername("client1");
        if (optionalClient1.isEmpty()) {
            User client1 = User.builder()
                    .email("client1.disi@gmail.ro")
                    .username("client1")
                    .password(passwordEncoder.encode("client1"))
                    .role(UserRole.CLIENT)
                    .build();

            UserProfile clientUserProfile1 = new UserProfile(client1, "Valerica", "Gaman");
            client1.setUserProfile(clientUserProfile1);

            userRepository.save(client1);
            userProfileRepository.save(clientUserProfile1);
        }

        Optional<User> optionalClient2 = userRepository.findByUsername("client2");
        if (optionalClient2.isEmpty()) {
            User client2 = User.builder()
                    .email("client2.disi@gmail.ro")
                    .username("client2")
                    .password(passwordEncoder.encode("client2"))
                    .role(UserRole.CLIENT)
                    .build();

            UserProfile clientUserProfile2 = new UserProfile(client2, "Denis", "Man");
            client2.setUserProfile(clientUserProfile2);

            userRepository.save(client2);
            userProfileRepository.save(clientUserProfile2);
        }
    }
}