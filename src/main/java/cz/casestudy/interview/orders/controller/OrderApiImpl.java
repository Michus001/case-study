package cz.casestudy.interview.orders.controller;


import cz.casestudy.interview.orders.model.Order;
import cz.casestudy.interview.order.rest.api.OrderApi;
import cz.casestudy.interview.order.rest.model.OrderWithId;
import cz.casestudy.interview.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@Slf4j
@Validated
@RestController
public class OrderApiImpl implements OrderApi {

    @NonNull
    private final OrderService orderService;

    @NonNull
    private final OrderMapper orderMapper;

    @Override
    public ResponseEntity<OrderWithId> createOrder(@Valid final cz.casestudy.interview.order.rest.model.Order order) {
        Assert.notNull(order, "order must not be null");

        Order createdOrder = orderService.createOrder(orderMapper.toProductBooking(order.getProducts()));
        return new ResponseEntity<>(orderMapper.toOrder(createdOrder, order.getProducts()), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> cancelOrder(UUID orderId) {
        Assert.notNull(orderId, "orderId must not be null");

        orderService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> payOrder(UUID orderId) {
        Assert.notNull(orderId, "orderId must not be null");

        orderService.payOrder(orderId);
        return ResponseEntity.noContent().build();
    }

}
