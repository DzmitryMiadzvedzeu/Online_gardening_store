package org.shop.com.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.shop.com.entity.HistoryEntity;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.repository.HistoryJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final HistoryJpaRepository repository;

    private final UserService userService;

    @Override
    public void add(OrderEntity order, UserEntity user) {
        log.debug("Adding history for user: {} and order: {}", user.getId(), order.getId());
        HistoryEntity history = new HistoryEntity();
        history.setOrder(order);
        history.setUser(user);
        repository.save(history);
    }

    @Override
    public List<HistoryEntity> get() {
        Long userId = userService.getCurrentUserId();
        log.debug("Retrieving history for user: {}", userId);
        List<HistoryEntity> history = repository.findByUser_Id(userId);
        return history;
    }
}