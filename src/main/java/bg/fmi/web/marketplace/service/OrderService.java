package bg.fmi.web.marketplace.service;

import bg.fmi.web.marketplace.exception.InvalidStateOfResourceException;
import bg.fmi.web.marketplace.exception.ResourceNotFoundException;
import bg.fmi.web.marketplace.model.Order;
import bg.fmi.web.marketplace.model.Status;
import bg.fmi.web.marketplace.model.OrderItem;
import bg.fmi.web.marketplace.model.Product;
import bg.fmi.web.marketplace.model.User;
import bg.fmi.web.marketplace.repository.OrderItemRepository;
import bg.fmi.web.marketplace.repository.OrderRepository;
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
    private final ProductService productService;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        OrderItemRepository orderItemRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
    }

    @Transactional
    public Order getPendingOrder(Long userId) {
        return createNewOrder(userId);
    }

    public List<Order> getOrderHistory(Long userId) {
        return orderRepository.findByUserIdAndStatusIn(userId, List.of(Status.COMPLETED, Status.CANCELED));
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
    public Order updateOrder(Long userId, Long productId, Integer quantity) {
        Order order = getPendingOrder(userId);

        if (order.getStatus() != Status.PENDING) {
            throw new InvalidStateOfResourceException("Cannot modify an order that is already " + order.getStatus());
        }

        Product product = productService.getProduct(productId);

        if (product.getUser().getId().equals(order.getUser().getId())) {
            throw new InvalidStateOfResourceException("You cannot add your own product to your cart.");
        }

        Optional<OrderItem> existingItem = orderItemRepository.findByOrderIdAndProductId(order.getId(), productId);

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
    public Order completeOrder(Long userId) {
        Order order = getPendingOrder(userId);

        if (order.getStatus() == Status.COMPLETED) {
            throw new InvalidStateOfResourceException("Order has already been completed");
        }

        // set product quantity to be prev q - items from the order
        order.getItems().forEach(orderItem -> {
            Product product = productService.getProduct(orderItem.getProduct().getId());
            int newQuantity = product.getQuantity() - orderItem.getQuantity();
            if (newQuantity < 0) {
                throw new InvalidStateOfResourceException(
                    "There is not enough items Product with id " + product.getId());
            }
            productService.updateQuantity(product.getId(), newQuantity);
        });
//TODO sum the prices of the items and update the VENDOR's revenue
//        order.getItems().

        order.setStatus(Status.COMPLETED);
        order.setOrderDate(LocalDate.now());

        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long userId) {
        Order order = getPendingOrder(userId);

        if (order.getStatus() == Status.COMPLETED) {
            order.getItems().forEach(orderItem -> {
                Product product = orderItem.getProduct();
                productService.updateQuantity(product.getId(), product.getQuantity() + orderItem.getQuantity());
            });
        }

        order.setStatus(Status.CANCELED);
        return orderRepository.save(order);
    }

}

