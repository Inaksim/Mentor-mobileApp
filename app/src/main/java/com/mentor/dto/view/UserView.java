package com.mentor.dto.view;


public class UserView {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstClass(String firstClass) {
        this.firstName = firstClass;
    }
}
