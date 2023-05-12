package com.example.javaee;

import java.util.Date;

public class Question {
    // Суррогатный ключ
    private Integer id;

    // Внешний ключ - ссылка на сущность User
    private Integer voteId;

    // Содержание вопроса
    private String content;

    // Дата голосования
    private Date dateVote;

    private Vote vote;

    // Констуктор без параметров
    public Question() {
    }

    //конструктор с параметрами
    public Question(Integer voteId, String content, Date dateVote) {
        this.voteId = voteId;
        this.content = content;
        this.dateVote = dateVote;
    }

    //конструктор с параметрами
    public Question(Integer voteId, String content, Date dateVote, Vote vote) {
        this.voteId = voteId;
        this.content = content;
        this.dateVote = dateVote;
        this.vote = vote;
    }


    //конструктор с параметрами
    public Question(Integer id, Integer voteId, String content, Date dateVote, Vote vote) {
        this.id = id;
        this.voteId = voteId;
        this.content = content;
        this.dateVote = dateVote;
        this.vote = vote;
    }


    //конструктор с параметрами
    public Question(Integer id, Integer voteId, String content, Date dateVote) {
        this.id = id;
        this.voteId = voteId;
        this.content = content;
        this.dateVote = dateVote;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public Vote vote () {
        return vote;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public java.sql.Date getDateVote() {
        return (java.sql.Date) dateVote;
    }
    public void setDateVote(Date dateVote) {
        this.dateVote = dateVote;
    }

    public Integer getVoteId() {
        return vote.getId();
    }
    public void setVoteId(Vote vote) {
        this.vote = vote;
    }

    @Override
    public String toString() {
        return "Question {" + "Id = " + id + ", VoteId = " + getVoteId() + ", Content = " + content + ", DateVote = " + dateVote + "}";
    }
}
