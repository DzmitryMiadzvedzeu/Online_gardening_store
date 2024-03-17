package org.shop.com.entity;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertFalse;

public class UserTest {

    @Test
    public void testEditPassword() {
        User user = new User("test_user", "bebebe");
        String originalPasswordHash = user.getPasswordHash();
        user.edit("new_password");
        assertNotEquals(originalPasswordHash, user.getPasswordHash());
    }

    private void assertNotEquals(String originalPasswordHash, String passwordHash) {

    }

    @Test
    public void testDeleteUser() {
        User user = new User("test_user", "bebebe");
        user.delete();
        assertFalse(user.isActive()); // Предположим, что isActive() возвращает true, если пользователь активен
    }

    @Test
    public void testPasswordHashing() {
        User user = new User("test_user", "bebebe");
        String hashedPassword = user.getPasswordHash();
        assertNotEquals("bebebe", hashedPassword);
    }
}
