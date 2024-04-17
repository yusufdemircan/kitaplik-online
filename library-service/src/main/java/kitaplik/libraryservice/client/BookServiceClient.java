package kitaplik.libraryservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import kitaplik.libraryservice.dto.BookDto;
import kitaplik.libraryservice.dto.BookIdDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service",path = "/v1/book")
public interface BookServiceClient {

    Logger logger = LoggerFactory.getLogger(BookServiceClient.class);

    @GetMapping("/isbn/{isbn}")
    @CircuitBreaker(name = "getBookByIsbnCircuitBreaker",fallbackMethod = "getBookByIsbnFallback")
    ResponseEntity<BookIdDto> getBookByIsbn(@PathVariable(value = "isbn") String isbn);

    default ResponseEntity<BookIdDto> getBookByIsbnFallback(String isbn,Exception exception){
        logger.info("Book not found by isbn " + isbn + ", returning default BookDto object");
        return ResponseEntity.ok(new BookIdDto("default bookId","default isbn"));
    }

    @GetMapping("/book/{bookId}")
    @CircuitBreaker(name = "getBookByIdCircuitBreaker" ,fallbackMethod = "getBookByIdFallback")
    ResponseEntity<BookDto> getBookById(@PathVariable(value = "bookId") String bookId);

    default ResponseEntity<BookDto> getBookByIdFallback(String bookId,Exception exception){
        logger.info("Book not found by bookId " + bookId + ", returning default BookDto object");
        return ResponseEntity.ok(new BookDto(new BookIdDto("default id","default isbn")));
    }


}
