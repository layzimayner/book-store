package com.example.demo.dto.user;

import com.example.demo.annotation.field_match.FieldMatch;
import com.example.demo.annotation.unique.Unique;
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
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String repeatPassword;
}
