package com.example.coffee_shop.securi_config;

import com.example.coffee_shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Разрешенные URL, которые не требуют аутентификации
    private static final String[] AUTH_WHITELIST = {
            "/login",
            "/logout",
            "/register",
    };

    @Autowired
    @Lazy
    private UserService userService;

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)  // Отключаем CSRF
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(AUTH_WHITELIST).permitAll()  // Разрешаем доступ к белому списку
                        .anyRequest().authenticated())  // Все остальные запросы требуют аутентификации
                .formLogin(form -> form
                        .loginPage("/login").loginProcessingUrl("/login").defaultSuccessUrl("/home", true)  // Страница входа
                        .permitAll())  // Разрешить всем доступ к странице входа
                .logout(logout -> logout
                        .logoutUrl("/logout")  // Страница выхода
                        .permitAll())  // Разрешить всем доступ к выходу
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Создавать сессию только если необходимо
                        .maximumSessions(1)  // Ограничиваем количество сессий для одного пользователя
                        .expiredUrl("/login?expired=true"));  // Страница для обработки истекшей сессии

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
