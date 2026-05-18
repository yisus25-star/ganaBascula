package com.ganabascula.config;

import com.ganabascula.entity.Rol;
import com.ganabascula.repository.RolRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleInitializer {

    @Bean
    CommandLineRunner initRoles(RolRepository rolRepository) {

        return args -> {

            if (rolRepository.findByNombre("ROLE_ADMIN").isEmpty()) {
                rolRepository.save(new Rol(null, "ROLE_ADMIN"));
            }

            if (rolRepository.findByNombre("ROLE_GANADERO").isEmpty()) {
                rolRepository.save(new Rol(null, "ROLE_GANADERO"));
            }

        };
    }

}