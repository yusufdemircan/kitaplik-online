package kitaplik.libraryservice.service;

import com.google.protobuf.Descriptors;
import com.kitaplik.bookservice.dto.*;
import io.grpc.stub.StreamObserver;
import kitaplik.libraryservice.client.BookServiceClient;
import kitaplik.libraryservice.dto.AddBookRequest;
import kitaplik.libraryservice.dto.BookDto;
import kitaplik.libraryservice.dto.BookIdDto;
import kitaplik.libraryservice.dto.LibraryDto;
import kitaplik.libraryservice.exception.LibraryNotFoundException;
import kitaplik.libraryservice.model.Library;
import kitaplik.libraryservice.repository.LibraryRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookServiceClient bookServiceClient;

    @GrpcClient("book-service")
    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;

    @GrpcClient("book-service")
    private BookServiceGrpc.BookServiceStub asyncBookServiceStub;

    public LibraryService(LibraryRepository libraryRepository, BookServiceClient bookServiceClient) {
        this.libraryRepository = libraryRepository;
        this.bookServiceClient = bookServiceClient;
    }

    public CompletableFuture<List<Map<Descriptors.FieldDescriptor, Object>>> getAllBooks() {
        CompletableFuture<List<Map<Descriptors.FieldDescriptor, Object>>> completableFuture = new CompletableFuture<>();
        final List<Map<Descriptors.FieldDescriptor, Object>> resp = new ArrayList<>();

        asyncBookServiceStub.getAllBooks(null, new StreamObserver<Book>() {
            @Override
            public void onNext(Book book) {
                resp.add(book.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {
                completableFuture.completeExceptionally(throwable);
            }

            @Override
            public void onCompleted() {
                completableFuture.complete(resp);
            }
        });
        return completableFuture;
    }

    public LibraryDto getAllBooksInLibraryById(String id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library could not found id : " + id));

        LibraryDto libraryDto = new LibraryDto(library.getId(),
                library.getUserBook().stream().map(bookServiceClient::getBookById)
                        .map(ResponseEntity::getBody)
                        .collect(Collectors.toList())
        );
        return libraryDto;
    }

    public LibraryDto createLibrary() {
        Library newLibrary = libraryRepository.save(new Library());
        return new LibraryDto(newLibrary.getId());
    }

    public void addBookToLibrary(AddBookRequest request) {
        //String bookId = bookServiceClient.getBookByIsbn(request.getIsbn()).getBody().getBookId();
        BookId bookIdByIsbn = bookServiceBlockingStub.getBookIdByIsbn(Isbn.newBuilder().setIsbn(request.getIsbn()).build());
        String bookId = bookIdByIsbn.getBookId();

        Library library = libraryRepository.findById(request.getId())
                .orElseThrow(() -> new LibraryNotFoundException("Library could not found id : " + bookId));
        library.getUserBook().add(bookId);
        libraryRepository.save(library);

    }
}
