package com.fastcampus.projectboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware(){
        return () -> Optional.of( "hhk");  // TODO : 스프링 시큐리티로 인증기능을 붙이게 될 때, 수정하자
    }                                            // of()안에 그냥 "hhk"만 넣으면된다. value를 쓰지 않아도 된다.
}
