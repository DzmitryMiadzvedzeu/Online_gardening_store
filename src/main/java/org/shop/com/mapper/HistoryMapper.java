package org.shop.com.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.shop.com.dto.HistoryDto;
import org.shop.com.entity.HistoryEntity;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "order.id", target = "orderId")
    HistoryDto toDto(HistoryEntity historyEntity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user.id", source = "historyDto.userId")
    @Mapping(target = "order.id", source = "historyDto.orderId")
    HistoryEntity toEntity(HistoryDto historyDto);
}