package com.example.javaee;

public class User {
    // Идентификатор пользователя
    private Integer id;

    // Имя
    private String fio;

    // Фамилия
    private String password;

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
    public User(String fio, String password, String email, String phone, Boolean status) {
        this.fio = fio;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    //конструктор с параметрами
    public User(Integer id, String fio, String password, String email, String phone, Boolean status) {
        this.id = id;
        this.fio = fio;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        return "User {" + "Id = " + id + ", Fio = " + fio + ", Password = " + password + ", Email = " + email + ", Phone = " + phone +  ", Status = " + status + "}";
    }
}
