package org.amelie.spring_security.dto;

import org.amelie.spring_security.entity.Book;

public record BookResponseDto(
        String id,
        String title,
        String author,
        String category,
        Integer publicationYear,
        Long numberOfCopies
) {

    public static BookResponseDto fromEntity(Book book) {
        return new BookResponseDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getPublicationYear(),
                book.getNumberOfCopies()
        );
    }
}
