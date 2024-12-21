package com.example.demo.service;

import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final Long TEST_BOOK_ID = 1L;
    private static final Long TEST_CATEGORY_ID = 1L;
    private static final String TEST_TITLE = "title1";
    private static final String TEST_AUTHOR = "Author1";
    private static final String TEST_ISBN = "1111111111";
    private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(11.1);
    private static final Set<Long> TEST_SET_CATEGORY_IDS = Set.of(1L);
    private static final String ALTER_TEST_TITLE = "title2";
    private static final String ALTER_TEST_AUTHOR = "Author2";
    private static final String ALTER_TEST_ISBN = "2222222222";
    private static final BigDecimal ALTER_TEST_PRICE = BigDecimal.valueOf(22.2);
    private static final Long ALTER_TEST_BOOK_ID = 2L;
    private static final Long UN_EXISTING_BOOK_ID = 3L;
    private static final Long EXPECTED_LENGTH = 2L;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Check saving service with valid request")
    void save_ValidRequest_retortDto() {
        Book bookForSave = createDefaultBook();
        CreateBookRequestDto requestDto = createDefaultBookRequestDto();
        BookDto expect = createDefaultBookDto();

        when(bookRepository.save(bookForSave)).thenReturn(bookForSave);
        when(bookMapper.toModel(requestDto)).thenReturn(bookForSave);
        when(bookMapper.toDto(bookForSave)).thenReturn(expect);

        BookDto actual = bookService.save(requestDto);

        Assertions.assertEquals(actual, expect);
    }

    @Test
    @DisplayName("Check functionality of findAll method")
    void findAll_BaseWithBooks_ReturnPageOfBooks() {
        Book firstBook = createDefaultBook();
        Book secondBook = createAlterDefaultBook();

        Pageable pageable = PageRequest.of(0, 10);

        when(bookRepository.findAll(pageable)).thenReturn(
                new PageImpl<>(List.of(firstBook, secondBook)));

        long actualLength = bookService.findAll(pageable).getTotalElements();
        long expectedLength = EXPECTED_LENGTH;
        Assertions.assertEquals(actualLength, expectedLength);
    }

    @Test
    @DisplayName("Delete non existing book")
    void delete_InvalidId_ThrowException() {
        Long id = UN_EXISTING_BOOK_ID;
        String expectMessage =  "Can't delete book with id " + id + " because it does not exist";

        when(bookRepository.existsById(id)).thenReturn(false);

        Exception exceptionMessage = assertThrows(EntityNotFoundException.class, () ->
                bookService.delete(id));

        String actual = exceptionMessage.getMessage();
        Assertions.assertEquals(expectMessage,actual);
    }

    @Test
    @DisplayName("Check updating service with valid request")
    void update_ValidRequest_ReturnBookDto() {
        Long bookId = TEST_BOOK_ID;
        CreateBookRequestDto requestDto = createDefaultBookRequestDto();
        Book existingBook = createAlterDefaultBook();
        BookDto expectedDto = createDefaultBookDto();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookMapper).updateBookFromDto(requestDto, existingBook);
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(bookMapper.toDto(existingBook)).thenReturn(expectedDto);

        BookDto actualDto = bookService.update(bookId, requestDto);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Check functionality of getBooksByCategoryId method")
    void getBooksByCategoryId_ValidId_ReturnListOfBooks() {
        Long categoryId = TEST_CATEGORY_ID;

        Book firstBook = createDefaultBook();
        Book secondBook = createAlterDefaultBook();

        BookDtoWithoutCategoryIds firstDto = createDefaultBookDtoWithoutIds();
        BookDtoWithoutCategoryIds secondDto = createAlterDefaultBookDtoWithoutIds();

        List<BookDtoWithoutCategoryIds> expectList = List.of(firstDto, secondDto);

        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(
                firstBook,secondBook));
        when(bookMapper.toDtoWithoutCategories(firstBook)).thenReturn(firstDto);
        when(bookMapper.toDtoWithoutCategories(secondBook)).thenReturn(secondDto);

        List<BookDtoWithoutCategoryIds> actualList = bookService.getBooksByCategoryId(categoryId);

        Assertions.assertEquals(actualList, expectList);
    }

    @Test
    @DisplayName("Check functionality of findById method")
    void findById_ExistingBookId_ReturnBookDto() {
        Long bookId = TEST_BOOK_ID;
        Book book = createDefaultBook();
        BookDto expectDto = createDefaultBookDto();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expectDto);

        BookDto actualDto = bookService.findById(bookId);

        Assertions.assertEquals(expectDto, actualDto);
    }

    private Book createDefaultBook() {
        Book book = new Book();
        book.setId(TEST_BOOK_ID);
        book.setAuthor(TEST_AUTHOR);
        book.setIsbn(TEST_ISBN);
        book.setPrice(TEST_PRICE);
        book.setTitle(TEST_TITLE);
        return book;
    }

    private Book createAlterDefaultBook() {
        Book book = new Book();
        book.setId(ALTER_TEST_BOOK_ID);
        book.setAuthor(ALTER_TEST_AUTHOR);
        book.setIsbn(ALTER_TEST_ISBN);
        book.setPrice(ALTER_TEST_PRICE);
        book.setTitle(ALTER_TEST_TITLE);
        return book;
    }

    private CreateBookRequestDto createDefaultBookRequestDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setCategoriesIds(TEST_SET_CATEGORY_IDS);
        requestDto.setAuthor(TEST_AUTHOR);
        requestDto.setTitle(TEST_TITLE);
        requestDto.setPrice(TEST_PRICE);
        requestDto.setIsbn(TEST_ISBN);
        return requestDto;
    }

    private BookDto createDefaultBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setCategoriesIds(TEST_SET_CATEGORY_IDS);
        bookDto.setId(TEST_BOOK_ID);
        bookDto.setAuthor(TEST_AUTHOR);
        bookDto.setIsbn(TEST_ISBN);
        bookDto.setPrice(TEST_PRICE);
        bookDto.setTitle(TEST_TITLE);
        return bookDto;
    }

    private BookDtoWithoutCategoryIds createDefaultBookDtoWithoutIds() {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(TEST_BOOK_ID);
        bookDto.setAuthor(TEST_AUTHOR);
        bookDto.setIsbn(TEST_ISBN);
        bookDto.setPrice(TEST_PRICE);
        bookDto.setTitle(TEST_TITLE);
        return bookDto;
    }

    private BookDtoWithoutCategoryIds createAlterDefaultBookDtoWithoutIds() {
        BookDtoWithoutCategoryIds bookDto = new BookDtoWithoutCategoryIds();
        bookDto.setId(ALTER_TEST_BOOK_ID);
        bookDto.setAuthor(ALTER_TEST_AUTHOR);
        bookDto.setIsbn(ALTER_TEST_ISBN);
        bookDto.setPrice(ALTER_TEST_PRICE);
        bookDto.setTitle(ALTER_TEST_TITLE);
        return bookDto;
    }
}
