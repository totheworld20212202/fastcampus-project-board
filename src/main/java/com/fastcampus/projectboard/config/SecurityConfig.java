package com.fastcampus.projectboard.config;

import com.fastcampus.projectboard.dto.UserAccountDto;
import com.fastcampus.projectboard.dto.security.BoardPrincipal;
import com.fastcampus.projectboard.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity    // 보통 이게 붙는데, spring boot에서 spring security연동시 불필요
@Configuration
public class SecurityConfig {   // boot 2.7, security 5.7 이상

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()
                                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                                .mvcMatchers(// antMatchers()와 같고 더 많은 기능 /form/**까지 포함.
                                        HttpMethod.GET,
                                        "/",
                                        "/articles",
                                        "/articles/search-hashtag"
                                ).permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin().and()
                .logout()
                    .logoutSuccessUrl("/")
                    .and()
                .build();

    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        // static resource, css - js 이런 파일들은 검증에서 제외하겠다.
////        return (web) -> web.ignoring().antMatchers("/css");
//        // 아래가 조금더 일반적인 정적 리소스 체크 제외방법
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());    // console warning : not recommended, 위에다가 추가. 그러면 spring securit관리하에 들어감
//    }

    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository){ // interface
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(()-> new UsernameNotFoundException(("유저를 찾을 수 없습니다 - username: " + username))); // UserDetailService에 들어가보면 거기서 UserDetails를 람다식으로 구현
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
