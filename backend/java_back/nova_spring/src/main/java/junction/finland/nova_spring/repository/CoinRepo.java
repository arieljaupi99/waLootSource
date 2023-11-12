package junction.finland.nova_spring.repository;

import junction.finland.nova_spring.entities.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoinRepo extends JpaRepository<Coin, Long> {
    List<Coin> findCoinsByUserIdIs(Long userId);
}
