package com.bot.gavial_bot.service;

import com.bot.gavial_bot.entity.User;
import com.bot.gavial_bot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public boolean hasUser(Long chatId){
        return userRepository.findByChatId(chatId) != null;
    }
    public void save(User user){
        userRepository.save(user);
    }
    public User getById(Long chatId){
        return userRepository.findByChatId(chatId);
    }

}
