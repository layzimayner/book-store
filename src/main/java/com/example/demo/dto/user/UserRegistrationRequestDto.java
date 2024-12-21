package com.example.demo.dto.user;

import com.example.demo.annotation.matcher.FieldMatch;
import com.example.demo.annotation.unique.Unique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@FieldMatch(
        firstField = "password",
        secondField = "repeatPassword",
        message = "Passwords must match")
public class UserRegistrationRequestDto {
    @NotBlank
    @Unique
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    private String shippingAddress;
}
