package com.yourcompany.mappers;

import com.yourcompany.DTO.BookDTO;
import com.yourcompany.entities.BookEntity;
import com.yourcompany.models.Book;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BookMapper implements IMapper<BookDTO, Book, BookEntity> {
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

    @Override
    public Book fromDTOtoModel(BookDTO DTO) {
        return new Book(
                DTO.getId(),
                DTO.getName(),
                DTO.getAuthor(),
                DTO.getDescription(),
                DTO.getPublished(),
                DTO.getCountInStock(),
                DTO.getPrice()
        );
    }

    @Override
    public BookDTO fromModelToDTO(Book model) {
        return new BookDTO(
                model.getId(),
                model.getName(),
                model.getAuthor(),
                model.getDescription(),
                model.getPublished(),
                model.isInStock(),
                model.getCountInStock(),
                model.getPrice()
        );
    }

    @Override
    public List<BookDTO> toDTOList(List<Book> models) {
        List<BookDTO> bookDTOList = new ArrayList<>();
        for (Book book : models) {
            bookDTOList.add(fromModelToDTO(book));
        }
        return bookDTOList;
    }
}
