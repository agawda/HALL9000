package com.javaacademy.robot.controllers;

import com.javaacademy.robot.helpers.FilterOrder;
import com.javaacademy.robot.model.BookDto;
import com.javaacademy.robot.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

/**
 * @author Anna Gawda
 *         08.09.2017
 */
@RestController
public class BookRestAPIController {

    private BookService bookService;

    @Autowired
    public BookRestAPIController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping("/api/books")
    public ResponseEntity<BookDto> bookRequestById(@RequestParam(value = "id") Long id) {
        BookDto foundBook = bookService.getBookByIsbn(id);
        if (foundBook == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bookService.getBookByIsbn(id));
    }

    @RequestMapping("/api/app")
    public List<BookDto> bookList() {
        return bookService.getAllBookDtos();
    }

    @RequestMapping("/api/search")
    public List<BookDto> searchResults(@RequestParam(value = "query") String query) {
        return bookService.searchOneKeyword(query.toLowerCase());
    }

    @RequestMapping("/api/advancedSearch")
    public ResponseEntity<List<BookDto>> advancedSearchController(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "bookstore", required = false) String bookstore,
            @RequestParam(value = "minPrice", required = false, defaultValue = "0") double minPrice,
            @RequestParam(value = "maxPrice", required = false, defaultValue = "1000000") double maxPrice) {

        List<BookDto> bookDtos = bookService.getByEverything(
                title.toLowerCase(),
                author.toLowerCase(),
                category.toLowerCase(),
                bookstore.toLowerCase(),
                minPrice,
                maxPrice);
        return ResponseEntity.ok(bookDtos);
    }

    @RequestMapping("/api/pages")
    public List<BookDto> getPage(@RequestParam(value = "id") int pageId) {
        return bookService.findAll(pageId);
    }

    @RequestMapping("/api/booksTotal")
    public int getBooksNumber() {
        return bookService.getAllBookDtos().size();
    }

    @RequestMapping("/api/sort")
    public ResponseEntity<List<BookDto>> sortedBooks(
            @RequestParam(value = "type") String type,
            @RequestParam(value = "order") String order,
            @RequestParam(value = "pageId") int pageId) {
        FilterOrder filterOrder = FilterOrder.valueOf(order.toUpperCase());
        List<BookDto> result = bookService.findAll(filterOrder, type, pageId);
        return Objects.nonNull(result) ?
                ResponseEntity.ok(result) :
                ResponseEntity.notFound().build();
    }
}
