package bg.fmi.web.marketplace.controller;

import bg.fmi.web.marketplace.dto.OrderItemResponseDto;
import bg.fmi.web.marketplace.dto.OrderResponseDto;
import bg.fmi.web.marketplace.dto.UpdateOrderRequestDto;
import bg.fmi.web.marketplace.model.order.Order;
import bg.fmi.web.marketplace.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody Long userId) {
        Order order = orderService.createNewOrder(userId);

        OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponseDto);
    }

    @PostMapping("orders/{orderId}/items")
    public ResponseEntity<OrderResponseDto> updateOrder(@RequestBody UpdateOrderRequestDto request, @PathVariable Long orderId) {
        Order order = orderService.updateOrder(orderId, request.getProductId(), request.getQuantity());

        OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);

        orderResponseDto.setItems(
                order.getItems().stream()
                .map(orderItem -> new OrderItemResponseDto(orderItem.getProduct().getId(), orderItem.getPrice(), orderItem.getQuantity()))
                .toList()
        );

        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    @PostMapping("orders/{orderId}/complete")
    public ResponseEntity<OrderResponseDto> completeOrder(@PathVariable Long orderId){
        Order order = orderService.completeOrder(orderId);

        OrderResponseDto completeOrder = modelMapper.map(order, OrderResponseDto.class);
        completeOrder.setItems(
                order.getItems().stream()
                .map(orderItem -> new OrderItemResponseDto(orderItem.getProduct().getId(), orderItem.getPrice(), orderItem.getQuantity()))
                .toList()
        );

        return ResponseEntity.status(HttpStatus.OK).body(completeOrder);
    }
}
