package com.fastcampus.projectboard.config;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties
    ) {
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.isDecoupledLogic());

        return defaultTemplateResolver;
    }


    // @ConfigurationProperties 여기에 빨간색 밑줄이 간 이유는 configuration을 user가 직접만들면 반드시 scan을 해주어야 함.
    // main class : fastcampusprojectboardapplication.java에 가서 @ConfigurationPropertiesScan을 추가해주어야 함.
//    @RequiredArgsConstructor
//    @Getter
    @ConstructorBinding
    @ConfigurationProperties("spring.thymeleaf3")
    public static class Thymeleaf3Properties {
        /**
         * Use Thymeleaf 3 Decoupled Logic
         */
        private final boolean decoupledLogic;
        public Thymeleaf3Properties(boolean decoupledLogic) {
            this.decoupledLogic = decoupledLogic;
        }

        public boolean isDecoupledLogic() {
            return this.decoupledLogic;
        }

    }
//    public Thymeleaf3Properties(boolean decoupledLogic) {
//        this.decoupledLogic = decoupledLogic;
//    }
//
//    public boolean isDecoupledLogic() {
//        return this.decoupledLogic;
//    }
}
