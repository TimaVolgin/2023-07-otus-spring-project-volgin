package ru.otus.spring.volgin.config;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import static java.util.Optional.ofNullable;

/**
 * Подключение keycloak
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers(HttpMethod.POST, "/admin/**").hasAnyRole("EDITOR", "ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/admin/**").hasAnyRole("EDITOR", "ADMIN")
                        .antMatchers("/admin/**").hasAnyRole("VIEWER", "EDITOR", "ADMIN")
                        .antMatchers("/user/**").permitAll()
                        .anyRequest().denyAll())
                .oauth2ResourceServer(resourceServerConfigurer -> resourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter())));
        return http.build();
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwt -> {
            var delegate = new JwtGrantedAuthoritiesConverter();
            var grantedAuthorities = delegate.convert(jwt);
            ofNullable((JSONObject) jwt.getClaim("resource_access"))
                    .map(resourceAccess -> (JSONObject) resourceAccess.get("shop-app"))
                    .map(shopApp -> (JSONArray) shopApp.get("roles"))
                    .map(roles -> roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                            .toList())
                    .ifPresent(grantedAuthorities::addAll);
            return grantedAuthorities;
        });
        return jwtAuthenticationConverter;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers("/actuator/health")
                .antMatchers("/swagger-ui.html")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/webjars/springfox-swagger-ui/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/v3/api-docs/**");
    }
}
