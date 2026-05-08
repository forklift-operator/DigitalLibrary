package bg.fmi.web.marketplace.repository;

import bg.fmi.web.marketplace.model.order.Order;
import bg.fmi.web.marketplace.model.order.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserIdAndStatus(Long userId, Status status);
}
