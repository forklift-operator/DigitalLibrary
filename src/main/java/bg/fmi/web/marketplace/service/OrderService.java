package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.exception.InvalidStateOfResourceException;
import bg.fmi.web.marketplace.exception.ResourceNotFoundException;
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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
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

    @Transactional
    public Order createNewOrder(Long userId) {
        return orderRepository.findByUserIdAndStatus(userId, Status.PENDING)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException(User.class.getSimpleName(), userId));

                    Order newOrder = new Order();
                    newOrder.setUser(user);
                    newOrder.setStatus(Status.PENDING);
                    newOrder.setTotalAmount(0.0);
                    newOrder.setItems(List.of());

                    return orderRepository.save(newOrder);
                });
    }

    @Transactional
    public Order updateOrder(Long orderId, Long productId, Integer quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class.getSimpleName(), orderId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), productId));

        Optional<OrderItem> existingItem = orderItemRepository.findByOrderIdAndProductId(orderId, productId);

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

    @Transactional
    public Order completeOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class.getSimpleName(), orderId));

        // need to check for owner

        if (order.getStatus() == Status.COMPLETED) {
            throw new InvalidStateOfResourceException("Order has already been completed");
        }

        // set product quantity to be prev q - items from the order
        order.getItems().forEach(orderItem -> {
            Product product = productRepository.findById(orderItem.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), orderItem.getProduct().getId()));
            int newQuantity = product.getQuantity() - orderItem.getQuantity();
            if (newQuantity < 0) {
                throw new InvalidStateOfResourceException("There is not enough items Product with id " + product.getId());
            }
            product.setQuantity(newQuantity);
            productRepository.save(product);
        });

        order.setStatus(Status.COMPLETED);
        order.setOrderDate(LocalDate.now());

        return orderRepository.save(order);
    }

    public Order cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(Order.class.getSimpleName(), orderId));

        order.setStatus(Status.CANCELED);

        return orderRepository.save(order);
    }

}

