package com.example.demo.controller;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.repository.CategoryRepository;
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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "classpath:database/books/insert-books-and-categories.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(scripts = "classpath:database/books/cleanup-db.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    private static final Long TEST_CATEGORY_ID = 3L;
    private static final String TEST_CATEGORY_NAME = "Category1";
    private static final String ALTER_TEST_CATEGORY_NAME = "UpdatedCategory";

    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setup(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Check createCategory endpoint by valid request")
    void createCategory_ValidRequest_ReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(TEST_CATEGORY_NAME);

        CategoryDto expectedCategory = new CategoryDto();
        expectedCategory.setId(TEST_CATEGORY_ID);
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
        EqualsBuilder.reflectionEquals(expectedCategory, actualCategory, "id");    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/books/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Check updateCategory endpoint by valid request")
    void updateCategory_ValidRequest_ReturnCategoryDto() throws Exception {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(ALTER_TEST_CATEGORY_NAME);

        CategoryDto expectedCategory = new CategoryDto();
        expectedCategory.setId(TEST_CATEGORY_ID);
        expectedCategory.setName(ALTER_TEST_CATEGORY_NAME);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/categories/{id}", TEST_CATEGORY_ID)
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

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Check deleteCategory endpoint by valid request")
    @Sql(scripts = "classpath:database/books/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteCategory_ValidRequest_NoContentStatus() throws Exception {
        mockMvc.perform(delete("/categories/{id}", TEST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = "classpath:database/books/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/delete-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Check getCategoryById endpoint")
    void getCategoryById_ValidId_ReturnCategoryDto() throws Exception {
        CategoryDto expectedCategory = new CategoryDto();
        expectedCategory.setId(TEST_CATEGORY_ID);
        expectedCategory.setName(TEST_CATEGORY_NAME);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}", TEST_CATEGORY_ID)
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

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Check getAllCategories endpoint")
    void getAllCategories_ReturnPageOfCategoryDto() throws Exception {
        CategoryDto firstCategory = new CategoryDto();
        firstCategory.setId(TEST_CATEGORY_ID);
        firstCategory.setName(TEST_CATEGORY_NAME);

        Page<CategoryDto> expectedPage = new PageImpl<>(List.of(firstCategory), PageRequest.of(0, 10), 1);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Page<CategoryDto> actualPage = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(PageImpl.class, CategoryDto.class)
        );

        assertEquals(expectedPage.getContent().size(), actualPage.getContent().size());
        assertEquals(expectedPage.getContent().get(0).getName(), actualPage.getContent().get(0).getName());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @DisplayName("Check getBooksByCategoryId endpoint")
    void getBooksByCategoryId_ReturnListOfBooks() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/categories/{id}/books", TEST_CATEGORY_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertNotNull(result.getResponse().getContentAsString());
    }
}
