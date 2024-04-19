package kitaplik.bookservice.service;

import com.kitaplik.bookservice.dto.*;
import io.grpc.stub.StreamObserver;
import kitaplik.bookservice.dto.BookDto;
import kitaplik.bookservice.dto.BookIdDto;
import kitaplik.bookservice.exception.BookNotFoundException;
import kitaplik.bookservice.repository.BookRepository;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService
public class BookGrpcService extends BookServiceGrpc.BookServiceImplBase {
    private static final Logger logger = LoggerFactory.getLogger(BookGrpcService.class);
    private final BookRepository bookRepository;

    public BookGrpcService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void getBookIdByIsbn(Isbn request, StreamObserver<BookId> responseObserver) {
        logger.info("Grpc call received: " + request.getIsbn());
        BookIdDto bookId = bookRepository.getBookByIsbn(request.getIsbn())
                .map(book -> new BookIdDto(book.getId(), book.getIsbn()))
                .orElseThrow(() -> new BookNotFoundException("Book could not found by isbn: " + request));
        responseObserver.onNext(
                BookId.newBuilder()
                        .setBookId(bookId.getBookId())
                        .setIsbn(bookId.getIsbn())
                        .build());
        responseObserver.onCompleted();

    }

    @Override
    public void getAllBooks(Empty request, StreamObserver<Book> responseObserver) {
        logger.info("Grpc call getAllBooks");
        List<BookDto> bookDtoList = bookRepository.findAll().stream().map(BookDto.Companion::convert).collect(Collectors.toList());
        //BookDto bk = bookDtoList.get(0);

        bookDtoList.stream().forEach(bk->
                responseObserver.onNext(
                        Book.newBuilder()
                                .setBookId(bk.getId().getBookId())
                                .setIsbn(bk.getId().getIsbn())
                                .setBookYear(bk.getBookYear())
                                .setPressName(bk.getPressName())
                                .setAuthor(bk.getAuthor())
                                .setTitle(bk.getTitle())
                                .build()));


        responseObserver.onCompleted();
    }
}
