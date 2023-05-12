package com.example.javaee;

import java.util.Date;

public class Choice {
    // Суррогатный ключ
    private Integer id;

    // Внешний ключ - ссылка на сущность Question
    private Integer questionId;

    // Внешний ключ - ссылка на сущность User
    private Integer userId;

    // Дата голосования
    private Integer choiceUser;

    private Question question;

    private User user;

    //конструктор без параметров
    public Choice() {
    }

    //конструктор с параметрами
    public Choice(Integer questionId, Integer userId, Integer choiceUser) {
        this.questionId = questionId;
        this.userId = userId;
        this.choiceUser = choiceUser;
    }

    //конструктор с параметрами
    public Choice(Integer id, Integer questionId, Integer userId, Integer choiceUser) {
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.choiceUser = choiceUser;
    }

    //конструктор с параметрами
    public Choice(Integer id, Integer questionId, Integer userId, Integer choiceUser, Question question, User user) {
        this.id = id;
        this.questionId = questionId;
        this.userId = userId;
        this.choiceUser = choiceUser;
        this.question = question;
        this.user = user;
    }

    //конструктор с параметрами
    public Choice(Integer questionId, Integer userId, Integer choiceUser, Question question, User user) {
        this.questionId = questionId;
        this.userId = userId;
        this.choiceUser = choiceUser;
        this.question = question;
        this.user = user;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionId() {
        return question.getId();
    }
    public void setQuestionId(Question question) {
        this.question = question;
    }

    public Integer getUserId() {
        return user.getId();
    }
    public void setUserId(User user) {
        this.user = user;
    }

    public Integer getChoiceUser() {
        return choiceUser;
    }
    public void setChoiceUser(Integer choiceUser) {
        this.choiceUser = choiceUser;
    }

    public Question question () {
        return question;
    }

    public User user () {
        return user;
    }

    @Override
    public String toString() {
        return "Choice {" + "Id = " + id + ", QustionId = " + getQuestionId() + ", UserId = " + getUserId() + ", ChoiceUser = " + choiceUser + "}";
    }
}
