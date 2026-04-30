package bg.fmi.web.marketplace.repository;

import bg.fmi.web.marketplace.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
