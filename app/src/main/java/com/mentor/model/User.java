package com.mentor.model;




public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String password;

    private String role;


    public User(Long id, String firstName, String lastName, String mail, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = mail;
        this.role = role;
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return firstName;
    }

    public void setName(String name) {
        this.firstName = name;
    }

    public String getMail() {
        return email;
    }

    public void setMail(String mail) {
        this.email = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
