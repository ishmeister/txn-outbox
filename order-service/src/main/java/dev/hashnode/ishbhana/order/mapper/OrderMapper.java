package dev.hashnode.ishbhana.order.mapper;

import dev.hashnode.ishbhana.order.domain.Order;
import dev.hashnode.ishbhana.order.dto.CreateOrderRequestDto;
import dev.hashnode.ishbhana.order.dto.CreateOrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    Order toOrder(CreateOrderRequestDto createOrderRequestDto);

    CreateOrderResponseDto toCreateOrderResponse(Order order);
}
