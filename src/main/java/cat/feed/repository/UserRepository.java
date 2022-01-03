package cat.feed.repository;

import cat.feed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String userId);

    Optional<User> findByEmail(String username);

    boolean existsByEmailAndType(String email, String type);

    User findByEmailAndType(String email, String type);

    List<User> findByRoles(String role);

    long countByRoles(String user);

    User findUserByEmail(String userId);
}
