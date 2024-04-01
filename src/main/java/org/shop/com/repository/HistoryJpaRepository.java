package org.shop.com.repository;

import org.shop.com.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

public interface HistoryJpaRepository extends JpaRepository<HistoryEntity, Long> {

    List<HistoryEntity> findByUser_Id(Long userId);
}
