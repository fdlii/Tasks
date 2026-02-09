package com.yourcompany.controllers;

import com.yourcompany.DTO.BookDTO;
import com.yourcompany.exceptions.BookNotFoundException;
import com.yourcompany.mappers.BookMapper;
import com.yourcompany.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;
    @Autowired
    BookMapper bookMapper;

    @GetMapping
    public List<BookDTO> getBooks() throws BookNotFoundException {
        return bookMapper.toDTOList(bookService.getBooks());
    }

    @GetMapping("/staled")
    public List<BookDTO> getStaledBooks() throws BookNotFoundException {
        return bookMapper.toDTOList(bookService.getStaledBooks());
    }

    @PostMapping
    public BookDTO addBook(@RequestBody BookDTO bookDTO) {
        bookService.addBook(
                bookDTO.getName(),
                bookDTO.getAuthor(),
                bookDTO.getDescription(),
                bookDTO.getPublished(),
                bookDTO.getPrice(),
                bookDTO.getCountInStock()
        );
        return bookDTO;
    }

    @DeleteMapping("/{book_name}")
    public void deleteBook(@PathVariable("book_name") String bookName) throws BookNotFoundException {
        bookService.deleteBook(bookName);
    }

    @PutMapping("/{book_name}/{count}")
    public void addInStock(@PathVariable("book_name") String bookName, @PathVariable("count") int count) throws BookNotFoundException {
        bookService.addInStock(bookName, count);
    }

    @PutMapping("/{book_name}")
    public void debitFromStock(@PathVariable("book_name") String bookName) throws BookNotFoundException {
        bookService.debitFromStock(bookName);
    }

    @GetMapping("/import")
    public void importFromCSV() throws IOException {
        bookService.importBooksFromCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Books.csv");
    }

    @GetMapping("/export")
    public void exportInCSV() throws IOException, BookNotFoundException {
        bookService.exportBooksIntoCSVFile("task_6/src/main/java/com/yourcompany/task_6_1/Books.csv");
    }
}
