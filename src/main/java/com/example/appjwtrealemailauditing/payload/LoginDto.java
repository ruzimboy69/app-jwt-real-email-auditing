package com.example.appjwtrealemailauditing.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDto {
    @Email
    @NotNull
    private String username;
    @NotNull
    private String password;
}
