package com.fastcampus.projectboard.config;


import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware(){
//        return () -> Optional.of("wernerk"); // TODO: modify this when spring security is used.
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
//                .map(x ->(BoardPrincipal) x)
                .map(BoardPrincipal.class::cast) // 위에 내용을 이렇게도 쓸수있음.
                .map(BoardPrincipal::getUsername);
                 // TODO: modify this when spring security is used.
    }
}
