package cat.feed.repository;

import cat.feed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUserId(String userId);

    Optional<User> findByUserId(String username);

    boolean existsByUserIdAndType(String email, String type);

    User findByUserIdAndType(String email,String type);

    List<User> findByRoles(String role);

    long countByRoles(String user);

    User findUserByUserId(String userId);
}
