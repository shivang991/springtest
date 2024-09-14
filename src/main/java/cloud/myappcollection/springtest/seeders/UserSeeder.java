package cloud.myappcollection.springtest.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import cloud.myappcollection.springtest.model.User;
import cloud.myappcollection.springtest.repository.UserRepository;

/**
 * Load default users into DB when application restarts
 */
@Component
public class UserSeeder implements CommandLineRunner {
    private final UserRepository userRepository;

    public UserSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            String defaultPasswd = new BCryptPasswordEncoder(12).encode("password");

            User[] users = {
                    new User(1, "Sohan", defaultPasswd),
                    new User(1, "Mohan", defaultPasswd),
            };
            userRepository.insert(users);
        }

    }
}
