package org.amelie.spring_security.controller;

import org.amelie.spring_security.dto.BookRequestDto;
import org.amelie.spring_security.dto.BookResponseDto;
import org.amelie.spring_security.entity.Book;
import org.amelie.spring_security.repository.BookRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepositoryInjected) {
        this.bookRepository = bookRepositoryInjected;
    }


    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN')")
    @GetMapping("")
    public List<BookResponseDto> getAll() {

        return this.bookRepository.findAll().stream()
                .map(BookResponseDto::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_ROLE_USER', 'SCOPE_ROLE_ADMIN')")
    @GetMapping("{id}")
    public BookResponseDto getById(@PathVariable String id) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livre introuvable : " + id));
        return BookResponseDto.fromEntity(book);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDto create(@RequestBody BookRequestDto request) {
            Book book = new Book();
            book.setTitle(request.title());
            book.setAuthor(request.author());
            book.setCategory(request.category());
            book.setPublicationYear(request.publicationYear());
            book.setNumberOfCopies(request.numberOfCopies());
            Book saved = this.bookRepository.save(book);
            return BookResponseDto.fromEntity(saved);
        }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @PutMapping("{id}")
    public BookResponseDto update(@PathVariable String id, @RequestBody BookRequestDto request) {
        Book book = this.bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Livre introuvable : " + id));
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setCategory(request.category());
        book.setPublicationYear(request.publicationYear());
        book.setNumberOfCopies(request.numberOfCopies());
        Book saved = this.bookRepository.save(book);
        return BookResponseDto.fromEntity(saved);
    }

    @PreAuthorize("hasAuthority('SCOPE_ROLE_ADMIN')")
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        this.bookRepository.deleteById(id);
    }
}
