package com.example.coffee_shop.service;


import com.example.coffee_shop.model.CoffeeShopUser;
import com.example.coffee_shop.model.UserBonus;
import com.example.coffee_shop.repository.UserBonusRepository;
import com.example.coffee_shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserBonusRepository userBonusRepository;

    public void createUser(CoffeeShopUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        UserBonus userBonus = new UserBonus();
        userBonus.setUser(user);
        userBonusRepository.save(userBonus);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("User searching for username: " + username);
        CoffeeShopUser user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return User.builder()
               .username(user.getUsername())
               .password(user.getPassword())
               .roles(user.getRole().getName())
                .build();
    }
}
