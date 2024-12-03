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
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ShoppingCartRepository cartRepository;
    private final CartMapper cartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public CartDto getCart(Long userId) {
        return cartMapper.toDto(cartRepository.getCartByUserId(userId));
    }

    @Transactional
    @Override
    public CartDto addItemToCart(CreateItemRequestDro requestDto,
                                 User user) {
        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId());

        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with ID: " + requestDto.getBookId()));

        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getBook().getId().equals(book.getId()))
                .findFirst()
                .orElse(null);

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setShoppingCart(shoppingCart);
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
                                      User user) {
        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId());

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with ID: " + id));

        Hibernate.initialize(shoppingCart.getCartItems());

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
                                   User user) {
        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId());

        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with ID: " + id));

        if (!shoppingCart.getCartItems().contains(cartItem)) {
            throw new EntityNotFoundException("Cart item does not belong to the user's cart");
        }
        shoppingCart.getCartItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }
}
