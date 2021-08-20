package cat.feed.repository;

import cat.feed.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    void create(){
        User user = new User();
        user.setUserName("123");
        user.setPassword(passwordEncoder.encode("123"));
        user.setNickName("123");
        user.setRoles("USER");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        System.out.println(userRepository.findAll());


    }

}