package kitaplik.libraryservice.controller;

import com.google.protobuf.Descriptors;
import com.kitaplik.bookservice.dto.Book;
import kitaplik.libraryservice.dto.AddBookRequest;
import kitaplik.libraryservice.dto.BookDto;
import kitaplik.libraryservice.dto.LibraryDto;
import kitaplik.libraryservice.service.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("getAllBooks")
    public CompletableFuture<List<BookDto>> getAllBooks() {
        return libraryService.getAllBooks();
    }

    @GetMapping("{id}")
    public ResponseEntity<LibraryDto> getLibraryById(@PathVariable String id) {
        return ResponseEntity.ok(libraryService.getAllBooksInLibraryById(id));
    }

    @PostMapping
    public ResponseEntity<LibraryDto> createLibrary() {
        return ResponseEntity.ok(libraryService.createLibrary());
    }


    @PutMapping
    public ResponseEntity<Void> addBookToLibrary(@RequestBody AddBookRequest request) {
        libraryService.addBookToLibrary(request);
        return ResponseEntity.ok().build();
    }
}
