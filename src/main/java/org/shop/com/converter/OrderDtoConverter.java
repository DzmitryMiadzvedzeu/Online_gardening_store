package org.shop.com.converter;
import org.shop.com.dto.OrderCreateDto;
import org.shop.com.dto.OrderDto;
import org.shop.com.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderDtoConverter implements OrderConverter<OrderEntity, OrderDto> {

    @Override
    public OrderDto toDto(OrderEntity orderEntity) {
        return new OrderDto(
                orderEntity.getId(),
                orderEntity.getDeliveryAddress(),
                orderEntity.getDeliveryMethod(),
                orderEntity.getContactPhone(),
                orderEntity.getStatus(),
                orderEntity.getCreatedAt(),
                orderEntity.getUpdatedAt()
        );
    }

    @Override
    public OrderEntity toEntity(OrderDto orderDto) {
        return new OrderEntity(
                orderDto.getDeliveryAddress(),
                orderDto.getDeliveryMethod(),
                orderDto.getContactPhone()
        );
    }

    @Override
    public OrderEntity createDtoToEntity(OrderCreateDto orderCreateDto) {
        return new OrderEntity(
                orderCreateDto.getDeliveryAddress(),
                orderCreateDto.getDeliveryMethod(),
                orderCreateDto.getContactPhone()
        );
    }
}
