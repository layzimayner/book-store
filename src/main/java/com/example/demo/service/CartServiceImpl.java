package com.example.demo.service;

import com.example.demo.dto.cart.CartDto;
import com.example.demo.dto.item.CartItemDto;
import com.example.demo.dto.item.CreateCartItemRequestDto;
import com.example.demo.dto.item.UpdateCartItemRequestDto;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.mapper.CartMapper;
import com.example.demo.model.Book;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final ShoppingCartRepository cartRepository;
    private final CartMapper cartMapper;
    private final BookRepository bookRepository;
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public CartDto getCart(Long userId) {
        return cartRepository.getCartByUserId(userId)
                .map(cart -> {
                    CartDto cartDto = cartMapper.toDto(cart);
                    cartDto.setCartItems(cartMapper.mapCartItemsToDto(cart.getCartItems()));
                    return cartDto;
                })
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user with ID: " + userId));
    }

    @Transactional
    @Override
    public CartItemDto addItemToCart(CreateCartItemRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user ID: " + user.getId()));

        Book book = bookRepository.findByIdWithCategories(requestDto.getBookId())
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
        return cartMapper.toCartItemDto(cartItem);
    }

    @Transactional
    @Override
    public CartItemDto updateItemQuantity(Long id,
                                                 UpdateCartItemRequestDto requestDto, User user) {
        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user ID: " + user.getId()));

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with ID: " + id + " in user's shopping cart"));

        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);
        return cartMapper.toCartItemDto(cartItem);
    }

    @Transactional
    @Override
    public void deleteItemFromCart(Long id, User user) {
        ShoppingCart shoppingCart = cartRepository.getCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Shopping cart not found for user ID: " + user.getId()));

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cart item not found with ID: " + id + " in user's shopping cart"));

        cartItemRepository.delete(cartItem);
    }

    @Transactional
    @Override
    public void createShoppingCartForUser(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
