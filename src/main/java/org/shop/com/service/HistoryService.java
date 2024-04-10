package org.shop.com.service;

import org.shop.com.entity.HistoryEntity;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;

import java.util.List;

public interface HistoryService {

    List<HistoryEntity> get();

    void add(OrderEntity order, UserEntity user);
}
