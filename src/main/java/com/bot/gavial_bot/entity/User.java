package com.bot.gavial_bot.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Quiz quiz;

    public User() {
    }

    public User(Long chatId, Quiz quiz) {
        this.chatId = chatId;
        this.quiz = quiz;
    }
}
