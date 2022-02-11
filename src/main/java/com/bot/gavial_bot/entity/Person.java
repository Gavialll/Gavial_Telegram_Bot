package com.bot.gavial_bot.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long chatId;
    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Quiz quiz;

    public Person() {
    }

    public Person(Long chatId, Quiz quiz) {
        this.chatId = chatId;
        this.quiz = quiz;
    }
}
