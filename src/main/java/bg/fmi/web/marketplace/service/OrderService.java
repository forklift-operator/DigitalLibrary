package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.model.order.Order;
import bg.fmi.web.marketplace.model.order.Status;
import bg.fmi.web.marketplace.model.orderItem.OrderItem;
import bg.fmi.web.marketplace.model.product.Product;
import bg.fmi.web.marketplace.model.user.User;
import bg.fmi.web.marketplace.repository.OrderItemRepository;
import bg.fmi.web.marketplace.repository.OrderRepository;
import bg.fmi.web.marketplace.repository.ProductRepository;
import bg.fmi.web.marketplace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public Order createNewOrder(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setStatus(Status.PENDING);
        order.setUser(user);

        return orderRepository.save(order);
    }

    public Order addProductToOrder(Long orderId, Long productId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        OrderItem orderItem = orderItemRepository.findByOrderIdAndProductId(order.getId())
                .orElseGet(() -> {
                    OrderItem newItem = new OrderItem();
                    newItem.setOrder(order);
                    newItem.setProduct(product);
                    newItem.setPrice(product.getPrice());
                    newItem.setQuantity(0);
                    return newItem;
                });

        orderItem.setQuantity(orderItem.getQuantity() + quantity);
        orderItemRepository.save(orderItem);

        order.setTotalAmount(order.getTotalAmount() * quantity + order.getTotalAmount());

        return orderRepository.save(order);
    }

}

