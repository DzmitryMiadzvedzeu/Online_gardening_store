package org.shop.com.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.shop.com.entity.HistoryEntity;
import org.shop.com.entity.OrderEntity;
import org.shop.com.entity.UserEntity;
import org.shop.com.repository.HistoryJpaRepository;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.*;

class HistoryServiceImplTest {

    @Mock
    private HistoryJpaRepository historyJpaRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private HistoryServiceImpl historyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addHistoryTest() {
        OrderEntity order = new OrderEntity();
        UserEntity user = new UserEntity();

        historyService.add(order, user);

        verify(historyJpaRepository, times(1)).save(any(HistoryEntity.class));
    }

    @Test
    void getUserHistoryTest() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);
        List<HistoryEntity> expectedHistory = Arrays.asList(new HistoryEntity(), new HistoryEntity());

        when(userService.getCurrentUserId()).thenReturn(userId);
        when(historyJpaRepository.findByUser_Id(userId)).thenReturn(expectedHistory);

        List<HistoryEntity> actualHistory = historyService.get();

        verify(userService, times(1)).getCurrentUserId();
        verify(historyJpaRepository, times(1)).findByUser_Id(userId);
        assertIterableEquals(expectedHistory, actualHistory);
    }
}