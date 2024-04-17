package com.mentor.model;

public class Course {

    private Long course_id;
    private String title;
    private String description;
    private String department;
    private int review;
    private Long category_id;
    private Long teacher_id;
    private boolean fav;
    private int members;
    private String link;
    private String picture;


    public Course(Long id, String title, String description, String department, int review, Long category_id, Long teacher_id, boolean isFav, int members, String link, String picture) {
        this.course_id = id;
        this.title = title;
        this.description = description;
        this.department = department;
        this.review = review;
        this.category_id = category_id;
        this.teacher_id = teacher_id;
        this.fav = isFav;
        this.members = members;
        this.link = link;
        this.picture = picture;
    }

    public Long getCategory_id() {
        return category_id;
    }

    public Long getTeacher_id() {
        return teacher_id;
    }

    public void setCategory_id(Long category_id) {
        this.category_id = category_id;
    }

    public void setTeacher_id(Long teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }

    public Long getCourse_id() {
        return course_id;
    }

    public void setCourse_id(Long course_id) {
        this.course_id = course_id;
    }

    public boolean isFav() {
        return fav;
    }

    public void setFav(boolean fav) {
        fav = fav;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
