package com.example.demo.service;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.item.CreateItemRequestDro;
import com.example.demo.dto.item.UpdateItemRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartDto getCart(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return cartMapper.toDto(cartRepository.getCart(user));
    }

    @Transactional
    @Override
    public CartDto addItemToCart(CreateItemRequestDro requestDto,
                                 Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = cartRepository.getCart(user);

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found with ID: " + requestDto.getBookId()));

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setSoppingCart(shoppingCart);
            cartItem.setBook(book);
            cartItem.setQuantity(requestDto.getQuantity());
            shoppingCart.getCartItems().add(cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + requestDto.getQuantity());
        }

        cartRepository.save(shoppingCart);
        return cartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public CartDto updateItemQuantity(Long id,
                                      UpdateItemRequestDto requestDto,
                                      Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = cartRepository.getCart(user);

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: " + id));

        if (!shoppingCart.getCartItems().contains(cartItem)) {
            throw new EntityNotFoundException("Cart item does not belong to the user's cart");
        }
        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return cartMapper.toDto(shoppingCart);
    }

    @Transactional
    @Override
    public void deleteItemFromCart(Long id,
                                   Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCart = cartRepository.getCart(user);

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found with ID: " + id));

        if (!shoppingCart.getCartItems().contains(cartItem)) {
            throw new EntityNotFoundException("Cart item does not belong to the user's cart");
        }
        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }
}
