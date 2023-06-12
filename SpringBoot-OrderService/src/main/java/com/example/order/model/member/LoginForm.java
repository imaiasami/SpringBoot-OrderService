package com.example.order.model.member;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class LoginForm {
    @Size(min = 4, max = 20)
    private String member_id;
    @Size(min = 4, max = 20)
    private String password;
}
