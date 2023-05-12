package com.example.javaee;

import java.sql.Date;

public class User {
    // Идентификатор пользователя
    private Integer id;

    // Имя
    private String firstName;

    // Фамилия
    private String lastName;

    // Телефон
    private String phone;

    // email
    private String email;

    // статус голосующего (проголосовал/нет)
    private Boolean status;

    //конструктор без параметров
    public User() {
    }

    //конструктор с параметрами
    public User(String firstName, String lastName, String phone, String email, Boolean status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    //конструктор с параметрами
    public User(Integer id, String firstName, String lastName, String phone, String email, Boolean status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User {" + "Id = " + id + ", FirstName = " + firstName + ", LastName = " + lastName + ", Phone = " + phone + ", Email = " + email + ", Status = " + status + "}";
    }
}
