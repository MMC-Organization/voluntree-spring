package com.voluntree.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.voluntree.backend.domain.volunteer.Cpf;
import com.voluntree.backend.domain.volunteer.Volunteer;
import com.voluntree.backend.repository.UserRepository;

@SpringBootApplication
public class BackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(BackendApplication.class, args);
  }

  @Bean
  CommandLineRunner initUserTest(UserRepository repo, PasswordEncoder encoder) {
    return args -> {
      if (!repo.existsByEmail("test@user.com")) {
        Volunteer vol = new Volunteer();

        vol.setName("arthur");
        vol.setEmail("test@user.com");
        vol.setPhoneNumber("998459868");
        vol.setCpf(new Cpf("15024982436"));
        vol.setCep("58052310");
        vol.setPassword(encoder.encode("euamocomercoco"));

        repo.save(vol);
      }
    };
  }

}
