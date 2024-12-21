package com.example.demo.service;

import com.example.demo.dto.category.CategoryDto;
import com.example.demo.dto.category.CreateCategoryRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final Long TEST_CATEGORY_ID = 1L;
    private static final String TEST_CATEGORY_NAME = "Category1";
    private static final String ALTER_TEST_CATEGORY_NAME = "Category2";
    private static final Long UN_EXISTING_CATEGORY_ID = 3L;
    private static final Long EXPECTED_LENGTH = 2L;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Check saving service with valid request")
    void save_ValidRequest_ReturnCategoryDto() {
        Category categoryForSave = createDefaultCategory();
        CreateCategoryRequestDto requestDto = createDefaultCreateCategoryRequestDto();
        CategoryDto expectedDto = createDefaultCategoryDto();

        when(categoryMapper.toEntity(requestDto)).thenReturn(categoryForSave);
        when(categoryRepository.save(categoryForSave)).thenReturn(categoryForSave);
        when(categoryMapper.toDto(categoryForSave)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.save(requestDto);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Check functionality of findAll method")
    void findAll_BaseWithCategories_ReturnPageOfCategories() {
        Category firstCategory = createDefaultCategory();
        Category secondCategory = createAlterDefaultCategory();

        Pageable pageable = PageRequest.of(0, 10);

        when(categoryRepository.findAll(pageable)).thenReturn(
                new PageImpl<>(List.of(firstCategory, secondCategory))
        );

        long actualLength = categoryService.findAll(pageable).getTotalElements();
        long expectedLength = EXPECTED_LENGTH;

        Assertions.assertEquals(expectedLength, actualLength);
    }

    @Test
    @DisplayName("Find category by existing id")
    void findById_ExistingCategoryId_ReturnCategoryDto() {
        Long categoryId = TEST_CATEGORY_ID;
        Category category = createDefaultCategory();
        CategoryDto expectedDto = createDefaultCategoryDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.getById(categoryId);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Find category by non-existing id")
    void findById_NonExistingCategoryId_ThrowException() {
        Long categoryId = UN_EXISTING_CATEGORY_ID;
        String expectedMessage = "Can't find category by id " + categoryId;

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                categoryService.getById(categoryId));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    @DisplayName("Update category with valid request")
    void update_ValidRequest_ReturnCategoryDto() {
        Long categoryId = TEST_CATEGORY_ID;
        CreateCategoryRequestDto requestDto = createDefaultCreateCategoryRequestDto();
        Category existingCategory = createDefaultCategory();
        CategoryDto expectedDto = createDefaultCategoryDto();

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        doNothing().when(categoryMapper).updateCategoryFromDto(requestDto, existingCategory);
        when(categoryRepository.save(existingCategory)).thenReturn(existingCategory);
        when(categoryMapper.toDto(existingCategory)).thenReturn(expectedDto);

        CategoryDto actualDto = categoryService.update(categoryId, requestDto);

        Assertions.assertEquals(expectedDto, actualDto);
    }

    @Test
    @DisplayName("Delete non-existing category")
    void delete_InvalidId_ThrowException() {
        Long categoryId = UN_EXISTING_CATEGORY_ID;
        String expectedMessage = "Can't delete category with id " + categoryId + " because it does not exist";

        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        Exception exception = assertThrows(EntityNotFoundException.class, () ->
                categoryService.deleteById(categoryId));

        Assertions.assertEquals(expectedMessage, exception.getMessage());
    }

    private Category createDefaultCategory() {
        Category category = new Category();
        category.setId(TEST_CATEGORY_ID);
        category.setName(TEST_CATEGORY_NAME);
        return category;
    }

    private Category createAlterDefaultCategory() {
        Category category = new Category();
        category.setId(2L);
        category.setName(ALTER_TEST_CATEGORY_NAME);
        return category;
    }

    private CreateCategoryRequestDto createDefaultCreateCategoryRequestDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(TEST_CATEGORY_NAME);
        return requestDto;
    }

    private CategoryDto createDefaultCategoryDto() {
        CategoryDto dto = new CategoryDto();
        dto.setId(TEST_CATEGORY_ID);
        dto.setName(TEST_CATEGORY_NAME);
        return dto;
    }
}
