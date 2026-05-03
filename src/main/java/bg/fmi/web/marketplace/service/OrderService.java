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

import java.util.Optional;

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
        order.setStatus(Status.PENDING);
        order.setTotalAmount(0.0);
        order.setUser(user);

        return orderRepository.save(order);
    }

    public Order updateOrder(Long orderId, Long productId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Optional<OrderItem> existingItem = orderItemRepository.findByOrderIdAndProductId(orderId, productId);

        System.out.println("ProdID: " + productId + " | ordID: " + orderId);

        existingItem.ifPresentOrElse(item -> {
                    if (quantity <= 0) {
                        System.out.println("ITEM IS PRESENT AND QUANTITY IS <= 0");

                        order.setTotalAmount(order.getTotalAmount() - item.getPrice() * item.getQuantity());
                        order.getItems().remove(item);

                        orderItemRepository.delete(item);
                    } else {
                        System.out.println("ITEM IS PRESENT AND QUANTITY IS POSITIVE");

                        Double priceDifference = item.getPrice() * (quantity - item.getQuantity());
                        item.setQuantity(quantity);

                        order.setTotalAmount(order.getTotalAmount() + priceDifference);

                        orderItemRepository.save(item);
                    }
                },

                () -> {
                    System.out.println("ITEM IS NOT PRESENT");
                    if (quantity > 0) {
                        OrderItem item = new OrderItem();
                        item.setOrder(order);
                        item.setProduct(product);
                        item.setPrice(product.getPrice());
                        item.setQuantity(quantity);

                        order.setTotalAmount(order.getTotalAmount() + item.getPrice() * quantity);
                        order.getItems().add(item);

                        orderItemRepository.save(item);
                    }
                });

        return orderRepository.save(order);
    }

}

