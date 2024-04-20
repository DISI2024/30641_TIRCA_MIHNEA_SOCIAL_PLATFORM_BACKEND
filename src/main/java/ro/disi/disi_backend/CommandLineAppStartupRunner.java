package ro.disi.disi_backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ro.disi.disi_backend.model.entity.User;
import ro.disi.disi_backend.model.enums.UserRole;
import ro.disi.disi_backend.repository.UserRepository;

import java.util.Optional;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CommandLineAppStartupRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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
            userRepository.save(admin);
        }
    }
}