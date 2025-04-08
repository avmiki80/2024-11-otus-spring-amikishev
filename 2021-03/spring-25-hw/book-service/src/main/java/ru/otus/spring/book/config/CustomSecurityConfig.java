package ru.otus.spring.book.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final String secret;

    @Autowired
    public CustomSecurityConfig(UserDetailsService userDetailsService, @Value("${jwt.secret:top-secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

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
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthenticationConverter())
                .and()
                .authenticationEntryPoint(bearerAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler());
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

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider() {
        return new JwtAuthenticationProvider(jwtDecoder());
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] key = secret.getBytes();
        var paddedKey = Arrays.copyOf(key, 128);
        SecretKey secretKey = new SecretKeySpec(paddedKey, MacAlgorithm.HS512.getName());
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }

    @Bean
    public AuthenticationEntryPoint bearerAuthenticationEntryPoint() {
        return new BearerTokenAuthenticationEntryPoint();
    }

//    @Bean
//    public AccessDeniedHandler accessDeniedHandler() {
//        return new BearerTokenAccessDeniedHandler();
//    }


    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
