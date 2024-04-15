package com.mentor.dto.form;

public class ResetPasswordForm {

    private String email;

    private String password;

    public ResetPasswordForm(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
