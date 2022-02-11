package com.bot.gavial_bot.service;

import com.bot.gavial_bot.entity.Person;
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
    public void save(Person person){
        userRepository.save(person);
    }
    public Person getById(Long chatId){
        return userRepository.findByChatId(chatId);
    }

}
