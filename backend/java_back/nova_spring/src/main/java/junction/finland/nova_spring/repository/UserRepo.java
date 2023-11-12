package junction.finland.nova_spring.repository;

import junction.finland.nova_spring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
}
