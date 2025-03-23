package ru.otus.spring.hw24.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
public class JwtConfig {
    private final String secret;
    @Autowired
    public JwtConfig(@Value("${jwt.secret:top-secret}") String secret) {
        this.secret = secret;
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
