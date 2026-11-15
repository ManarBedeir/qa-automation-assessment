package com.manarbedeir.qa.api.services;

import com.manarbedeir.qa.api.models.Book;
import io.restassured.response.Response;

public class BooksService extends BaseService {

    private static final String BOOKS = "/Books";
    private static final String BOOK_BY_ID = "/Books/{id}";

    public Response getAllBooks() {
        return request().get(BOOKS);
    }

    public Response getBook(Object id) {
        return request().get(BOOK_BY_ID, id);
    }

    public Response createBook(Book book) {
        return request().body(book).post(BOOKS);
    }

    public Response updateBook(int id, Book book) {
        return request().body(book).put(BOOK_BY_ID, id);
    }

    public Response deleteBook(int id) {
        return request().delete(BOOK_BY_ID, id);
    }
}
