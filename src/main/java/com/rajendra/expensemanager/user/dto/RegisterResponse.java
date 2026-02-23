package com.rajendra.expensemanager.user.dto;

import lombok.Getter;

@Getter
public class RegisterResponse {
    private String email;

    public RegisterResponse(String email) {
        this.email = email;
    }
}
