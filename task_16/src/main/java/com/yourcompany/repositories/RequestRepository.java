package com.yourcompany.repositories;

import com.yourcompany.entities.BookEntity;
import com.yourcompany.entities.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    List<RequestEntity> findByBook(BookEntity book);
}
