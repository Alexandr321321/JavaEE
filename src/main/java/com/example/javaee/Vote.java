package com.example.javaee;

import java.util.Date;

public class Vote {
    // Суррогатный ключ
    private Integer id;

    // Тема голосования
    private String title;

    // Дата начала голосования
    private Date dateStart;

    // Дата окончания голосования
    private Date dateFinish;

    // Статус голосования
    private Boolean status;

    // Констуктор без параметров
    public Vote() {
    }

    //конструктор с параметрами
    public Vote(String title, Date dateStart, Date dateFinish, Boolean status) {
        this.title = title;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.status = status;
    }

    //конструктор с параметрами
    public Vote(Integer id, String title, Date dateStart, Date dateFinish, Boolean status) {
        this.id = id;
        this.title = title;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.status = status;
    }

    //конструктор с параметрами
    public Vote(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public java.sql.Date getDateStart() {
        return (java.sql.Date) dateStart;
    }
    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public java.sql.Date getDateFinish() {
        return (java.sql.Date) dateFinish;
    }
    public void setDateFinish(Date dateFinish) {
        this.dateFinish = dateFinish;
    }

    public Boolean getStatus() {
        return status;
    }
    public void setStatus(Boolean status) {
        this.status = status;
    }



    @Override
    public String toString() {
        return "Vote {" + "Id = " + id + ", Title = " + title + ", DateStart = " + dateStart + ", DateFinish = " + dateFinish + ", Status = " + status + "}";
    }
}
