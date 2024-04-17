package kitaplik.bookservice.service;
import kitaplik.bookservice.dto.BookDto;
import kitaplik.bookservice.dto.BookIdDto;
import kitaplik.bookservice.exception.BookNotFoundException;
import kitaplik.bookservice.repository.BookRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    @Cacheable(cacheNames = "books")
    public List<BookDto> getAllBooks() {
        return repository.findAll().stream().map(BookDto.Companion::convert).collect(Collectors.toList());

    }

    public BookIdDto findByIsbn(String isbn) {
        return repository.getBookByIsbn(isbn)
                .map(book ->  new BookIdDto(Objects.requireNonNull(book.getId()), book.getIsbn()))
                .orElseThrow(() -> new BookNotFoundException("Book could not found by isbn: " + isbn));
    }

    public BookDto findBookDetailsById(String id) {
        return repository.findById(id)
                .map(BookDto.Companion::convert)
                .orElseThrow(() -> new BookNotFoundException("Book could not found by id:" + id));
    }
}