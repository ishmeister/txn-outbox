package dev.hashnode.ishbhana.order.controller;

import dev.hashnode.ishbhana.order.domain.Order;
import dev.hashnode.ishbhana.order.dto.CreateOrderRequestDto;
import dev.hashnode.ishbhana.order.dto.CreateOrderResponseDto;
import dev.hashnode.ishbhana.order.mapper.OrderMapper;
import dev.hashnode.ishbhana.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<CreateOrderResponseDto> createOrder(
            @Valid @RequestBody CreateOrderRequestDto requestDto) {
        Order order = orderService.createOrder(requestDto);
        CreateOrderResponseDto response = orderMapper.toCreateOrderResponse(order);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
