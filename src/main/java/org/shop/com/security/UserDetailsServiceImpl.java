package org.shop.com.security;

import org.shop.com.entity.UserEntity;
import org.shop.com.exceptions.UserNotFoundException;
import org.shop.com.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            UserEntity userEntity = userService.getByLogin(username);
            return new User(userEntity.getName(), userEntity.getPasswordHash(),
                    List.of(new SimpleGrantedAuthority(userEntity.getRole().getRoleName())));
        } catch (UserNotFoundException exception) {
            throw new UsernameNotFoundException(exception.getMessage());
        }
    }
}