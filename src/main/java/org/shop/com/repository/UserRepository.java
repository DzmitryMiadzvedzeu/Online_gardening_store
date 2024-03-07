package org.shop.com.repository;

import org.shop.com.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    // Найти по роли
    List<User> findByRole(String role);
}
