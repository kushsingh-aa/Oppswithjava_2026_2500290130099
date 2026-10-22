package com.kiet.library.model;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String category;
    private int pubilcationYear;
    private boolean available;

    public Book(int bookId, String title, String author, String category, int pubilcationYear, boolean available) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.category = category;
        this.pubilcationYear = pubilcationYear;
        this.available = available;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPublicationYear() {
        return pubilcationYear;
    }

    public void setPublicationYear(int pubilcationYear) {
        this.pubilcationYear = pubilcationYear;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
