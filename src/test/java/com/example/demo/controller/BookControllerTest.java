package com.example.demo.controller;

import com.example.demo.config.PageImplMixin;
import com.example.demo.dto.book.BookDto;
import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.book.CreateBookRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.repository.BookRepository;
import com.example.demo.service.BookService;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    private static final Long FIRST_BOOK_ID = 1L;
    private static final Long SECOND_BOOK_ID = 2L;
    private static final Long THIRD_BOOK_ID = 3L;
    private static final Long TEST_BOOK_ID = 4L;
    private static final int NUMBER_OF_PAGES = 0;
    private static final int PAGE_SIZE = 10;
    private static final String TEST_TITLE = "title1";
    private static final String TEST_AUTHOR = "Author1";
    private static final String TEST_ISBN = "1111111111";
    private static final BigDecimal TEST_PRICE = BigDecimal.valueOf(11.1);
    private static final Set<Long> TEST_CATEGORIES = Set.of(1L);
    private static final String ALTER_TEST_TITLE = "title2";
    private static final String ALTER_TEST_AUTHOR = "Author2";
    private static final String ALTER_TEST_ISBN = "2222222222";
    private static final BigDecimal ALTER_TEST_PRICE = BigDecimal.valueOf(22.2);
    private static final Set<Long> ALTER_TEST_CATEGORIES = Set.of(2L);

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    BookService bookService;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext webApplicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Check createBook endpoint by valid request")
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_validRequestDto_ReturnBookDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(TEST_TITLE);
        requestDto.setAuthor(TEST_AUTHOR);
        requestDto.setIsbn(TEST_ISBN);
        requestDto.setPrice(TEST_PRICE);
        requestDto.setCategoriesIds(TEST_CATEGORIES);

        BookDto expect = createDefaultBookDto();

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expect, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Check functionality of getAllBooks method")
    @Sql(scripts = "classpath:database/books/insert-books-and-categories.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllBooks_DbWithData_ReturnPageOfBookDtoWithoutCategoryIds()
            throws Exception {
        Pageable pageable = PageRequest.of(NUMBER_OF_PAGES, PAGE_SIZE);

        BookDtoWithoutCategoryIds firstDto = findAndMap(FIRST_BOOK_ID);
        BookDtoWithoutCategoryIds secondDto = findAndMap(SECOND_BOOK_ID);
        BookDtoWithoutCategoryIds thirdDto = findAndMap(THIRD_BOOK_ID);

        Page<BookDtoWithoutCategoryIds> expected = new PageImpl<>(List.of(
                firstDto, secondDto, thirdDto), pageable, 3);

        objectMapper.addMixIn(PageImpl.class, PageImplMixin.class);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JavaType pageType = objectMapper.getTypeFactory()
                .constructParametricType(PageImpl.class, BookDtoWithoutCategoryIds.class);
        Page<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), pageType);

        assertEquals(expected.getContent(), actual.getContent());
        assertEquals(expected.getTotalElements(), actual.getTotalElements());
        assertEquals(expected.getNumber(), actual.getNumber());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/add-book.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Check functionality of deleteBook method")
    void deleteBook_ValidData_ReturnNoContentStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", TEST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        assertFalse(bookRepository.existsById(TEST_BOOK_ID));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/add-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Check functionality of updateBook method")
    void update_ValidData_ReturnBookDto() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(ALTER_TEST_TITLE);
        requestDto.setAuthor(ALTER_TEST_AUTHOR);
        requestDto.setIsbn(ALTER_TEST_ISBN);
        requestDto.setPrice(ALTER_TEST_PRICE);
        requestDto.setCategoriesIds(ALTER_TEST_CATEGORIES);

        BookDto expect = new BookDto();
        expect.setTitle(ALTER_TEST_TITLE);
        expect.setAuthor(ALTER_TEST_AUTHOR);
        expect.setIsbn(ALTER_TEST_ISBN);
        expect.setPrice(ALTER_TEST_PRICE);
        expect.setCategoriesIds(ALTER_TEST_CATEGORIES);
        expect.setId(TEST_BOOK_ID);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/books/{id}", TEST_BOOK_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expect, actual);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Check functionality of findBookById method")
    @Sql(scripts = "classpath:database/books/add-book.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/cleanup-db.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findBookById_ValidId_ReturnBookDto() throws Exception {
        BookDto expect = createDefaultBookDto();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", TEST_BOOK_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expect, actual);
    }

    private BookDtoWithoutCategoryIds findAndMap(Long id) {
        return bookMapper.toDtoWithoutCategories(bookRepository.
                findById(id).orElseThrow(() ->
                        new EntityNotFoundException("Can't find book by id " + id)));
    }

    private BookDto createDefaultBookDto() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(TEST_TITLE);
        bookDto.setAuthor(TEST_AUTHOR);
        bookDto.setIsbn(TEST_ISBN);
        bookDto.setPrice(TEST_PRICE);
        bookDto.setCategoriesIds(TEST_CATEGORIES);
        bookDto.setId(TEST_BOOK_ID);
        return  bookDto;
    }
}
