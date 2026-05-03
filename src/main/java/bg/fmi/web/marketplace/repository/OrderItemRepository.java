package bg.fmi.web.marketplace.repository;

import bg.fmi.web.marketplace.model.orderItem.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByOrderIdAndProductId(Long orderId);
}
