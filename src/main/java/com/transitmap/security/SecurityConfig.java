package com.transitmap.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuration de la sécurité Spring Security.
 * Définit les règles d'accès par rôle pour chaque endpoint.
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final LoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

                // Accès public
                .requestMatchers(
                    "/", "/login", "/map",
                    "/inscription/**",
                    "/api/**",
                    "/css/**", "/js/**",
                    "/images/**", "/webjars/**"
                ).permitAll()

                // Admin uniquement
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Agent uniquement
                .requestMatchers("/agent/**").hasRole("AGENT")

                // Chauffeur uniquement
                .requestMatchers("/chauffeur/**").hasRole("CHAUFFEUR")

                // Voyageur + Admin + Agent
                .requestMatchers("/voyageur/**")
                    .hasAnyRole("VOYAGEUR", "ADMIN", "AGENT")

                // Interurbain
                .requestMatchers("/interurbain/**")
                    .hasAnyRole("VOYAGEUR", "ADMIN", "AGENT")

                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureUrl("/login?erreur=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?deconnexion=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(userDetailsService);

        return http.build();
    }

    /** Encodeur de mots de passe BCrypt */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /** Gestionnaire d'authentification */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}