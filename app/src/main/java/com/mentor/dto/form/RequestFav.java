package com.mentor.dto.form;

public class RequestFav {
    private Long categoryId;
    private String email;

    public RequestFav(Long categoryId, String email) {
        this.categoryId = categoryId;
        this.email = email;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
