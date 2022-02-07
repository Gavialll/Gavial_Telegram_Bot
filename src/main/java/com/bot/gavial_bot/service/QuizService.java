package com.bot.gavial_bot.service;

import com.bot.gavial_bot.model.Quiz;
import com.bot.gavial_bot.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

    public Quiz getById(Long id){
        return quizRepository.getById(id);
    }
    public void save(Quiz quiz){
        quizRepository.save(quiz);
    }
}
