package org.shop.com.service;

import lombok.extern.slf4j.Slf4j;
import org.shop.com.entity.HistoryEntity;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.repository.HistoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryJpaRepository repository;
    private final UserService userService;

    @Autowired
    public HistoryServiceImpl(HistoryJpaRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
        log.debug("HistoryServiceImpl initialized with repository: {} and userService: {}",
                repository.getClass().getSimpleName(), userService.getClass().getSimpleName());
    }

    @Override
    public void addHistory(OrderEntity order, UserEntity user) {
        log.debug("Adding history for user: {} and order: {}", user.getId(), order.getId());
        HistoryEntity history = new HistoryEntity();
        history.setOrder(order);
        history.setUser(user);
        log.info("History added for user: {} and order: {}", user.getId(), order.getId());
        repository.save(history);
    }

    @Override
    public List<HistoryEntity> getUserHistory() {
        Long userId = userService.getCurrentUserId();
        log.debug("Retrieving history for user: {}", userId);
        List<HistoryEntity> history = repository.findByUser_Id(userId);
        log.info("Retrieved {} history records for user: {}", history.size(), userId);
        return history;
    }
}
