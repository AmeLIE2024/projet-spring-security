package org.amelie.spring_security.init;

import org.amelie.spring_security.entity.Book;
import org.amelie.spring_security.entity.Role;
import org.amelie.spring_security.entity.UserEntity;
import org.amelie.spring_security.repository.BookRepository;
import org.amelie.spring_security.repository.RoleRepository;
import org.amelie.spring_security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {


    private final RoleRepository roleRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(
            RoleRepository roleRepositoryInjected,
            BookRepository bookRepositoryInjected,
            UserRepository userRepositoryInjected,
            PasswordEncoder passwordEncoderInjected) {
        this.roleRepository = roleRepositoryInjected;
        this.bookRepository = bookRepositoryInjected;
        this.userRepository = userRepositoryInjected;
        this.passwordEncoder = passwordEncoderInjected;
    }

    @Value("${app.init.admin-password}")
    private String adminPassword;

    @Value("${app.init.user-password}")
    private String userPassword;


    @Override
    public void run(String... args) throws Exception {
        this.bookRepository.save(new Book("Vertige", "Franck THILLIEZ", "thriller", 2011, 100000));
        this.bookRepository.save(new Book("Le Petit Prince", "Antoine de Saint-Exupéry", "fairy tale", 1943, 200000));
        this.bookRepository.save(new Book("1984", "George Orwell", "dystopian", 1949, 150000));

        Role roleUser = new Role();
        roleUser.setAuthority("ROLE_USER");
        roleRepository.save(roleUser);

        Role roleAdmin = new Role();
        roleAdmin.setAuthority("ROLE_ADMIN");
        roleRepository.save(roleAdmin);


    UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setAuthorities(Set.of(roleAdmin));
        userRepository.save(admin);

    UserEntity user = new UserEntity();
        user.setUsername("bastien");
        user.setEmail("bastien@example.com");
        user.setPassword(passwordEncoder.encode(userPassword));
        user.setAuthorities(Set.of(roleUser));
        userRepository.save(user);
}
}
