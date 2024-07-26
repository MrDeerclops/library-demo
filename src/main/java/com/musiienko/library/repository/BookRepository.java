package com.musiienko.library.repository;

import com.musiienko.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT b FROM Book b WHERE LOWER(b.author) LIKE CONCAT('%', LOWER(?1), '%') " +
            "OR LOWER(b.title) LIKE CONCAT('%', LOWER(?1), '%')")
    List<Book> findByAuthorOrTitleMatch(String match);
}
