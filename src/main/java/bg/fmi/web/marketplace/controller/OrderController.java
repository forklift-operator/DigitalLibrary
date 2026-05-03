package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.OrderItemDto;
import bg.fmi.web.marketplace.dto.OrderResponseDto;
import bg.fmi.web.marketplace.dto.UpdateOrderRequestDto;
import bg.fmi.web.marketplace.model.order.Order;
import bg.fmi.web.marketplace.model.order.Status;
import bg.fmi.web.marketplace.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody Long userId) {
        Order order = orderService.createNewOrder(userId);

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setStatus(Status.PENDING);
        orderResponseDto.setItems(List.of());
        orderResponseDto.setTotalAmount(order.getTotalAmount());

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @PostMapping("orders/{id}/items")
    public ResponseEntity<OrderResponseDto> updateOrder(@RequestBody UpdateOrderRequestDto request) {
        Order order = orderService.updateOrder(request.getOrderId(), request.getProductId(), request.getQuantity());

        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setId(order.getId());
        orderResponseDto.setStatus(Status.PENDING);
        orderResponseDto.setItems(order.getItems().stream()
                .map(orderItem -> new OrderItemDto(orderItem.getProduct().getId(), orderItem.getPrice(), orderItem.getQuantity()))
                .toList());
        orderResponseDto.setTotalAmount(order.getTotalAmount());

        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }


}
