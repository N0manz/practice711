package mirea.practice711.security;

import lombok.RequiredArgsConstructor;
import mirea.practice711.security.jwt.JwtFilter;
import mirea.practice711.service.client.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, CustomUserDetailsService userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        // =========================
                        // PUBLIC (гость)
                        // =========================
                        .requestMatchers(
                                "/login",
                                "/register",
                                "/auth/login",
                                "/auth/register",
                                "/css/**",
                                "/js/**"
                        ).permitAll()

                        // =========================
                        // USER (просмотр)
                        // =========================
                        .requestMatchers(HttpMethod.GET, "/products/**")
                        .hasAnyRole("USER", "SELLER", "ADMIN")

                        .requestMatchers("/auth/me")
                        .hasAnyRole("USER", "SELLER", "ADMIN")

                        // =========================
                        // SELLER
                        // =========================
                        .requestMatchers(HttpMethod.POST, "/products")
                        .hasRole("SELLER")

                        .requestMatchers(HttpMethod.POST, "/products/*")
                        .hasRole("SELLER")

                        .requestMatchers(HttpMethod.GET, "/products/*/edit")
                        .hasRole("SELLER")

                        // =========================
                        // ADMIN
                        // =========================
                        .requestMatchers(HttpMethod.GET, "/users/**")
                        .hasAnyRole("USER", "SELLER", "ADMIN")

                        .requestMatchers(HttpMethod.POST, "/products/*/delete")
                        .hasRole("ADMIN")

                        // =========================
                        .anyRequest().authenticated()
                )

                // редирект на логин если не авторизован
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((req, res, e) ->
                                res.sendRedirect("/login")
                        )
                )

                .userDetailsService(userDetailsService)

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}