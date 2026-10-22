package com.kiet.library.service;

import com.kiet.library.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    private List<Book> books = new ArrayList<>();

    public BookService() {
        books.add(new Book(1, "The Great Gatsby", "F. Scott Fitzgerald", "Fiction", 1925, true));
        books.add(new Book(2, "To Kill a Mockingbird", "Harper Lee", "Fiction", 1960, true));
        books.add(new Book(3, "1984", "George Orwell", "Dystopian", 1949, false));
    }

    // get all books
    public List<Book> getAllBooks() {
        return books;
    }

    // get book by id
    public Book getBookById(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                return book;
            }
        }
        return null;
    }

    // Add a new book
    public Book addBook(Book book) {
        books.add(book);
        return book;
    }

    // Update a book
    public Book updateBook(int bookId, Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId() == bookId) {
                updatedBook.setBookId(bookId);
                books.set(i, updatedBook);

                return updatedBook;
            }
        }
        return null;
    }

    // Delete a book
    public boolean deleteBook(int bookId) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getBookId() == bookId) {
                books.remove(i);
                return true;
            }
        }
        return false;
    }

    // serach books by category
    public List<Book> searchBooksByCategory(String category) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getCategory().equalsIgnoreCase(category)) {
                result.add(book);
            }
        }
        return result;
    }

    // Find books published from a specific year
    public List<Book> findBooksPublishedFromYear(int year) {
        List<Book> result = new ArrayList<>();
        for (Book book : books) {
            if (book.getPublicationYear() >= year) {
                result.add(book);
            }
        }
        return result;
    }
}
