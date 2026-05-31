package com.transitmap.config;

import com.transitmap.entity.Role;
import com.transitmap.entity.User;
import com.transitmap.repository.RoleRepository;
import com.transitmap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // إنشاء الأدوار إن لم تكن موجودة
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("ADMIN").build()));

        Role agentRole = roleRepository.findByName("AGENT")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("AGENT").build()));

        Role voyageurRole = roleRepository.findByName("VOYAGEUR")
                .orElseGet(() -> roleRepository.save(
                        Role.builder().name("VOYAGEUR").build()));

        // إنشاء مستخدم Admin افتراضي
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .enabled(true)
                    .role(adminRole)
                    .build());
        }

        // إنشاء Agent تجريبي
        if (!userRepository.existsByUsername("agent1")) {
            userRepository.save(User.builder()
                    .username("agent1")
                    .password(passwordEncoder.encode("agent123"))
                    .enabled(true)
                    .role(agentRole)
                    .build());
        }

        // إنشاء Voyageur تجريبي
        if (!userRepository.existsByUsername("voyageur1")) {
            userRepository.save(User.builder()
                    .username("voyageur1")
                    .password(passwordEncoder.encode("voyageur123"))
                    .enabled(true)
                    .role(voyageurRole)
                    .build());
        }
    }
}