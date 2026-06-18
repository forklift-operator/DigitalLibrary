package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.OrderItemResponseDto;
import bg.fmi.web.marketplace.dto.OrderResponseDto;
import bg.fmi.web.marketplace.dto.OrderUpdateRequestDto;
import bg.fmi.web.marketplace.exception.UnauthorisedException;
import bg.fmi.web.marketplace.model.Order;
import bg.fmi.web.marketplace.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@SessionAttribute("USER_ID") Long userId) {
        if (userId == null) {
            throw new UnauthorisedException();
        }

        Order order = orderService.createNewOrder(userId);

        OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @PutMapping("orders/update")
    public ResponseEntity<OrderResponseDto> updateOrder(@RequestBody OrderUpdateRequestDto request, @SessionAttribute("USER_ID") Long userId) {
        Order order = orderService.updateOrder(userId, request.getProductId(), request.getQuantity());

        OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);

        orderResponseDto.setItems(
                order.getItems().stream()
                .map(orderItem -> new OrderItemResponseDto(orderItem.getProduct().getId(), orderItem.getPrice(), orderItem.getQuantity()))
                .toList()
        );

        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @PostMapping("orders/{orderId}/complete")
    public ResponseEntity<OrderResponseDto> completeOrder(@SessionAttribute("USER_ID") Long userId){
        Order order = orderService.completeOrder(userId);

        OrderResponseDto completeOrder = modelMapper.map(order, OrderResponseDto.class);
        completeOrder.setItems(
                order.getItems().stream()
                .map(orderItem -> new OrderItemResponseDto(orderItem.getProduct().getId(), orderItem.getPrice(), orderItem.getQuantity()))
                .toList()
        );

        return ResponseEntity.status(HttpStatus.OK).body(completeOrder);
    }

    @GetMapping("orders/history")
    public ResponseEntity<OrderResponseDto[]> getHistory(@SessionAttribute("USER_ID") Long userId) {
        List<Order> orders = orderService.getOrderHistory(userId);

        OrderResponseDto[] orderHistory = modelMapper.map(orders, OrderResponseDto[].class);

        return ResponseEntity.status(HttpStatus.OK).body(orderHistory);
    }
}
