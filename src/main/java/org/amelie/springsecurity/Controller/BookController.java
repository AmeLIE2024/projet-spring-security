package org.amelie.springsecurity.Controller;

import org.amelie.springsecurity.Entity.Book;
import org.amelie.springsecurity.Repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepositoryInjected) {
        this.bookRepository = bookRepositoryInjected;
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN')")
    @GetMapping("")
    public List<Book> getAll() {
        return this.bookRepository.findAll();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN')")
    @GetMapping("{id}")
    public Optional<Book> getById(@PathVariable String id) {
        return this.bookRepository.findById(id);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return this.bookRepository.save(book);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping("{id}")
    public Book update(@PathVariable String id, @RequestBody Book book) {
        book.setId(id);
        return this.bookRepository.save(book);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        this.bookRepository.deleteById(id);
    }
}
