package com.mentor.dto.form;

public class RequestFav {
    private Long course_id;
    private String email;

    public RequestFav(Long course_id, String email) {
        this.course_id = course_id;
        this.email = email;
    }

//    public Long getCourse_id() {
//        return course_id;
//    }
//
//    public void setCourse_id(Long categoryId) {
//        this.course_id = categoryId;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
}
