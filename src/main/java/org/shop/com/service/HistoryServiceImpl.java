package org.shop.com.service;

import org.shop.com.entity.HistoryEntity;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.repository.HistoryJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryJpaRepository repository;
    private final UserService userService;

    @Autowired
    public HistoryServiceImpl(HistoryJpaRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    @Override
    public void addHistory(OrderEntity order, UserEntity user) {
        HistoryEntity history = new HistoryEntity();
        history.setOrder(order);
        history.setUser(user);
        repository.save(history);
    }

    @Override
    public List<HistoryEntity> getUserHistory() {
        Long userId = userService.getCurrentUserId();
        return repository.findByUser_Id(userId);
    }
}
