package com.kiet.library.controller;

import com.kiet.library.model.Book;
import com.kiet.library.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // 1. Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // 2. Get book by ID
    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(
            @PathVariable int bookId) {

        Book book = bookService.getBookById(bookId);

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\":\"Book not found\"}");
        }

        return ResponseEntity.ok(book);
    }

    // 3. Add a new book
    @PostMapping
    public ResponseEntity<Book> addBook(
            @RequestBody Book book) {

        Book savedBook = bookService.addBook(book);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedBook);
    }

    // 4. Update a book
    @PutMapping("/{bookId}")
    public ResponseEntity<?> updateBook(
            @PathVariable int bookId,
            @RequestBody Book updatedBook) {

        Book book = bookService.updateBook(bookId, updatedBook);

        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\":\"Book not found\"}");
        }

        return ResponseEntity.ok(book);
    }

    // 5. Delete a book
    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(
            @PathVariable int bookId) {

        boolean deleted = bookService.deleteBook(bookId);

        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found");
        }

        return ResponseEntity.ok("Book deleted successfully");
    }

    // 6. Search books by category
    @GetMapping("/search")
    public List<Book> searchByCategory(String category) {
        List<Book> result = new ArrayList<>();

        for (Book book : bookService.getAllBooks()) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                result.add(book);
            }
        }

        return result;
    }

    // 7. Filter books by publication year
    @GetMapping("/published")
    public List<Book> findPublishedFromYear(int fromYear) {
        List<Book> result = new ArrayList<>();

        for (Book book : bookService.getAllBooks()) {
            if (book.getPublicationYear() >= fromYear) {
                result.add(book);
            }
        }

        return result;
    }
}