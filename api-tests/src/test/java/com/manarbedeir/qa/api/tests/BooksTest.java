package com.manarbedeir.qa.api.tests;

import com.manarbedeir.qa.api.data.ExistingBook;
import com.manarbedeir.qa.api.data.InvalidBookIdCase;
import com.manarbedeir.qa.api.models.Book;
import com.manarbedeir.qa.api.services.BooksService;
import com.manarbedeir.qa.commons.data.TestData;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class BooksTest {

    private static final String DATA_FILE = "testdata/books.json";
    private static final String BOOK_SCHEMA = "schemas/book-schema.json";

    private final BooksService booksService = new BooksService();

    @DataProvider(name = "existingBooks")
    public Object[][] existingBooks() {
        return TestData.asRows(TestData.readList(DATA_FILE, "existingBooks", ExistingBook.class));
    }

    @DataProvider(name = "newBooks")
    public Object[][] newBooks() {
        return TestData.asRows(TestData.readList(DATA_FILE, "newBooks", Book.class));
    }

    @DataProvider(name = "invalidBookIds")
    public Object[][] invalidBookIds() {
        return TestData.asRows(TestData.readList(DATA_FILE, "invalidBookIds", InvalidBookIdCase.class));
    }

    @Test(dataProvider = "existingBooks")
    public void shouldReturnBookById(ExistingBook expected) {
        Book book = booksService.getBook(expected.id())
                .then()
                .statusCode(HttpStatus.SC_OK)
                .contentType(ContentType.JSON)
                .body(matchesJsonSchemaInClasspath(BOOK_SCHEMA))
                .extract().as(Book.class);

        assertEquals(book.id(), expected.id(), "Book id");
        assertEquals(book.title(), expected.title(), "Book title");
        assertTrue(book.pageCount() > 0, "pageCount should be positive");
    }

    @Test(dataProvider = "newBooks")
    public void shouldCreateBook(Book newBook) {
        Book created = booksService.createBook(newBook)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(matchesJsonSchemaInClasspath(BOOK_SCHEMA))
                .extract().as(Book.class);

        assertEquals(created, newBook, "Created book should match the request");
    }

    @Test(dataProvider = "invalidBookIds")
    public void shouldRejectRequestForInvalidBookId(InvalidBookIdCase testCase) {
        booksService.getBook(testCase.id())
                .then()
                .statusCode(testCase.expectedStatus())
                .contentType(startsWith("application/problem+json"))
                .body("status", equalTo(testCase.expectedStatus()))
                .body("title", equalTo(testCase.expectedTitle()));
    }
}
