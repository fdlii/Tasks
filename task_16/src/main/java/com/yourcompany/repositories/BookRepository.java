package com.yourcompany.repositories;

import com.yourcompany.entities.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookEntity, Long> {
    BookEntity findByName(String name);
    @Query(value = """
        SELECT b.* 
        FROM books b
        WHERE NOT EXISTS (
            SELECT 1 
            FROM orders_books ob
            JOIN orders o ON ob.orderid = o.id
            WHERE ob.bookid = b.id 
              AND o.executiondate >= :threshold
        )
        """,
        nativeQuery = true)
    List<BookEntity> getStaledBooks(@Param("threshold") LocalDateTime threshold);
}
