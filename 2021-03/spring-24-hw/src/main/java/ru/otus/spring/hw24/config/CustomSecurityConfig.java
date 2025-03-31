package ru.otus.spring.hw24.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;



@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@RequiredArgsConstructor
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final AuthenticationEntryPoint bearerAuthenticationEntryPoint;
    private final AccessDeniedHandler customAccessDeniedHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/book", "/book/**").hasRole("USER")
                .antMatchers("/genre", "/genre/**", "/author", "/author/**", "/book", "/book/**").hasRole("ADMIN")
                .antMatchers("/comment", "/comment/**").hasAnyRole("ADMIN", "USER")
                .antMatchers("/actuator/**", "/role/**", "/user/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter)
                .and()
                .authenticationEntryPoint(bearerAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler);
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .failureForwardUrl("/error");
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @ConditionalOnMissingBean(UserDetailsService.class)
    public InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> inMemoryProvider(){
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> provider = new InMemoryUserDetailsManagerConfigurer<>();
        provider
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("USER", "ADMIN")
                .and()
                .withUser("user")
                .password(passwordEncoder().encode("user"))
                .roles("USER");
        return provider;
    }
    @Bean
    @ConditionalOnBean(UserDetailsService.class)
    public DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> daoAuthenticationConfigurer(){
        DaoAuthenticationConfigurer<AuthenticationManagerBuilder, UserDetailsService> daoAuthenticationConfigurer =
                new DaoAuthenticationConfigurer<>(userDetailsService);
        daoAuthenticationConfigurer.passwordEncoder(passwordEncoder());
        return daoAuthenticationConfigurer;
    }


}
