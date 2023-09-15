package com.tpe.cookerytech.init;

import com.tpe.cookerytech.domain.Role;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.repository.UserRepository;
import com.tpe.cookerytech.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Component
public class MyRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final RoleService roleService;

    public MyRunner(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    @Bean
    public CommandLineRunner init(UserRepository userRepository) {
        return args -> {
          //createAdminUser(userRepository);
            //createCustomerUser(userRepository);
        };
    }
    public void createAdminUser(UserRepository userRepository) {
        User adminUser = new User();
        adminUser.setFirstName("Admin1");
        adminUser.setLastName("Admin");
        adminUser.setEmail("admin1@gmail.com");
        adminUser.setPhone("123456789");
        adminUser.setAddress("123 Main St");
        adminUser.setBuiltIn(true);
        adminUser.setCity("New York");
        adminUser.setCountry("USA");
        adminUser.setBirthDate(LocalDate.of(1990, 1, 1));
        adminUser.setResetPasswordCode(null);
        adminUser.setCreateAt(LocalDateTime.now());
        adminUser.setUpdateAt(null);
        adminUser.setStatus((byte) 0);
        adminUser.setPasswordHash(passwordEncoder.encode("Pass*123"));

        Role role = roleService.findByType(RoleType.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        adminUser.setRoles(roles);

        userRepository.save(adminUser);

        System.out.println("Admin user created and saved to the database!");
    }

    public void createCustomerUser(UserRepository userRepository){

        User customerUser = new User();
        customerUser.setFirstName("Customer1");
        customerUser.setLastName("Customer");
        customerUser.setEmail("customer1@gmail.com");
        customerUser.setPhone("987654321");
        customerUser.setAddress("123 Main St");
        customerUser.setBuiltIn(true);
        customerUser.setCity("New York");
        customerUser.setCountry("USA");
        customerUser.setBirthDate(LocalDate.of(1996, 2, 2));
        customerUser.setResetPasswordCode(null);
        customerUser.setCreateAt(LocalDateTime.now());
        customerUser.setUpdateAt(null);
        customerUser.setStatus((byte) 0);
        customerUser.setPasswordHash(passwordEncoder.encode("Pass*12345"));

        Role role = roleService.findByType(RoleType.ROLE_ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        customerUser.setRoles(roles);

        userRepository.save(customerUser);

        System.out.println("Customer user created and saved to the database!");




    }
}
