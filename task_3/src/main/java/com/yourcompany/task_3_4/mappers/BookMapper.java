package com.yourcompany.task_3_4.mappers;

import com.yourcompany.models.Book;
import com.yourcompany.task_13.entities.BookEntity;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookMapper implements IMapper<Book, BookEntity> {
    @Override
    public Book toModel(BookEntity entity) {
        return new Book(
                entity.getId(),
                entity.getName(),
                entity.getAuthor(),
                entity.getDescription(),
                Date.from(entity.getPublished().atZone(ZoneId.systemDefault()).toInstant()),
                entity.getCountInStock(),
                entity.getPrice()
        );
    }

    @Override
    public BookEntity toEntity(Book model, boolean ignoreId) {
        if (ignoreId) {
            return new BookEntity(
                    model.getName(),
                    model.getAuthor(),
                    model.getDescription(),
                    model.getPublished().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                    model.isInStock(),
                    model.getCountInStock(),
                    model.getPrice()
            );
        }
        return new BookEntity(
                model.getId(),
                model.getName(),
                model.getAuthor(),
                model.getDescription(),
                model.getPublished().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(),
                model.isInStock(),
                model.getCountInStock(),
                model.getPrice()
        );
    }

    @Override
    public List<Book> toModelsList(List<BookEntity> entities) {
        List<Book> books = new ArrayList<>();
        for (BookEntity entity : entities) {
            books.add(toModel(entity));
        }
        return books;
    }
}
