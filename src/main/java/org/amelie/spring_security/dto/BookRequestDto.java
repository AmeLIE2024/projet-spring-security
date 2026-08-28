package org.amelie.spring_security.dto;

public record BookRequestDto(
        String title,
        String author,
        String category,
        Integer publicationYear,
        Long numberOfCopies) {
}
