package cz.casestudy.interview.orders.controller;

import cz.casestudy.interview.orders.model.Order;
import cz.casestudy.interview.order.rest.model.OrderWithId;
import cz.casestudy.interview.order.rest.model.OrderedProduct;
import cz.casestudy.interview.products.api.ProductBooking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collection;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    Set<ProductBooking> toProductBooking(Collection<OrderedProduct> orders);

    ProductBooking toProductBooking(OrderedProduct product);

    @Mapping(target = "id", source = "createdOrder.orderId")
    @Mapping(target = "products", source = "orders")
    OrderWithId toOrder(Order createdOrder, Collection<OrderedProduct> orders);
}
