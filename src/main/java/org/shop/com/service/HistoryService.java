package org.shop.com.service;

import org.shop.com.entity.HistoryEntity;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;

import java.util.List;

public interface HistoryService {

    List<HistoryEntity> getUserHistory();

    void addHistory(OrderEntity order, UserEntity user);
}
