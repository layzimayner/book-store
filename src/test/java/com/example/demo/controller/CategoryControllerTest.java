package com.example.demo.controller;

import com.example.demo.dto.book.BookDtoWithoutCategoryIds;
import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.repository.CategoryRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import javax.sql.DataSource;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static final Long NEW_TEST_CATEGORY_ID = 3L;
    private static final Long TEST_CATEGORY_ID = 1L;
    private static final String TEST_CATEGORY_NAME = "Category1";
    private static final String ALTER_TEST_CATEGORY_NAME = "UpdatedCategory";
    private static final int EXPECTED_LENGTH = 2;
    private static final String PAGE_PARAM_NAME = "page";
    private static final String PAGE_PARAM_VALUE = "0";
    private static final String SIZE_PARAM_NAME = "size";
    private static final String SIZE_PARAM_VALUE = "2";

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeAll
    static void setup(@Autowired WebApplicationContext webApplicationContext,
                      @Autowired DataSource dataSource) throws SQLException {
        cleanUpDb(dataSource);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach
    void beforeEach(@Autowired DataSource dataSource) throws SQLException{
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/scripts/insert-books-and-categories.sql"));
        }
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) {
        cleanUpDb(dataSource);
    }

    @SneakyThrows
    static void cleanUpDb(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/scripts/cleanup-db.sql")
            );
        }
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Check createCategory endpoint by valid request")
    void createCategory_ValidRequest_ReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(TEST_CATEGORY_NAME);

        CategoryDto expectedCategory = new CategoryDto();
        expectedCategory.setId(NEW_TEST_CATEGORY_ID);
        expectedCategory.setName(TEST_CATEGORY_NAME);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryDto actualCategory = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );

        Assertions.assertNotNull(actualCategory);
        EqualsBuilder.reflectionEquals(expectedCategory, actualCategory, "id");
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/scripts/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Check updateCategory endpoint by valid request")
    void updateCategory_ValidRequest_ReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(ALTER_TEST_CATEGORY_NAME);

        CategoryDto expectedCategory = new CategoryDto();
        expectedCategory.setId(NEW_TEST_CATEGORY_ID);
        expectedCategory.setName(ALTER_TEST_CATEGORY_NAME);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", NEW_TEST_CATEGORY_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualCategory = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedCategory.getName(), actualCategory.getName());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @DisplayName("Check deleteCategory endpoint by valid request")
    @Sql(scripts = "classpath:database/scripts/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategory_ValidRequest_NoContentStatus() throws Exception {
        mockMvc.perform(delete("/categories/{id}", NEW_TEST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
        Assertions.assertFalse(categoryRepository.existsById(NEW_TEST_CATEGORY_ID));
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @Sql(scripts = "classpath:database/scripts/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Check getCategoryById endpoint")
    void getCategoryById_ValidId_ReturnCategoryDto() throws Exception {
        CategoryDto expectedCategory = new CategoryDto();
        expectedCategory.setId(NEW_TEST_CATEGORY_ID);
        expectedCategory.setName(TEST_CATEGORY_NAME);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", NEW_TEST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CategoryDto actualCategory = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                CategoryDto.class
        );

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedCategory.getName(), actualCategory.getName());
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Check getAllCategories endpoint")
    void getAllCategories_ReturnPageOfCategoryDto() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .param(PAGE_PARAM_NAME, PAGE_PARAM_VALUE)
                        .param(SIZE_PARAM_NAME, SIZE_PARAM_VALUE)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode root = objectMapper.readTree(result.getResponse().getContentAsString());
        List<CategoryDto> actualContent = objectMapper.readValue(
                root.get("content").toString(),
                new TypeReference<List<CategoryDto>>() {}
        );

        Assertions.assertNotNull(actualContent);
        Assertions.assertFalse(actualContent.isEmpty());
        Assertions.assertEquals(EXPECTED_LENGTH, actualContent.size());
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Check getBooksByCategoryId endpoint")
    void getBooksByCategoryId_ReturnListOfBooks() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/categories/{id}/books", TEST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        List<BookDtoWithoutCategoryIds> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<BookDtoWithoutCategoryIds>>() {}
        );
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EXPECTED_LENGTH, actual.size());
    }
}