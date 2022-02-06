package com.bot.gavial_bot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;
    private String name;

    public Long getChatId() {
        return chatId;
    }

    public User setChatId(Long chatId) {
        this.chatId = chatId;
        return User.this;
    }

    public User() {
    }

    public User(Long chatId, String name) {
        this.chatId = chatId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return User.this;
    }

    @Override
    public String toString() {
        return "User{" + "chatId=" + chatId + ", name='" + name + '\'' + '}';
    }
}
