package org.amelie.springsecurity.init;

import org.amelie.springsecurity.Entity.Book;
import org.amelie.springsecurity.Entity.Role;
import org.amelie.springsecurity.Entity.UserEntity;
import org.amelie.springsecurity.Repository.BookRepository;
import org.amelie.springsecurity.Repository.RoleRepository;
import org.amelie.springsecurity.Repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Datainitializer implements CommandLineRunner {


    private final RoleRepository roleRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Datainitializer(
            RoleRepository roleRepositoryInjected,
            BookRepository bookRepositoryInjected,
            UserRepository userRepositoryInjected,
            PasswordEncoder passwordEncoderInjected) {
        this.roleRepository = roleRepositoryInjected;
        this.bookRepository = bookRepositoryInjected;
        this.userRepository = userRepositoryInjected;
        this.passwordEncoder = passwordEncoderInjected;
    }


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
        admin.setPassword(passwordEncoder.encode("securepassword"));
        admin.setAuthorities(Set.of(roleAdmin));
        userRepository.save(admin);

    UserEntity user = new UserEntity();
        user.setUsername("bastien");
        user.setEmail("bastien@example.com");
        user.setPassword(passwordEncoder.encode("tacostacos"));
        user.setAuthorities(Set.of(roleUser));
        userRepository.save(user);
}
}
