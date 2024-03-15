package org.shop.com.entity;

import org.testng.annotations.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testEditPassword() {
        User user = new User("test_user", "bebebe");
        String originalPasswordHash = user.getPasswordHash();
        user.edit("new_password");
        assertNotEquals(originalPasswordHash, user.getPasswordHash());
    }

    @Test
    public void testDeleteUser() {
        // Добавьте тесты для удаления пользователя
    }

    @Test
    public void testPasswordHashing() {
        User user = new User("test_user", "bebebe");
        String hashedPassword = user.getPasswordHash();
        assertNotEquals("bebebe", hashedPassword);
    }
}
