package com.bot.gavial_bot.service;

import com.bot.gavial_bot.model.Word;
import com.bot.gavial_bot.repository.WordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordService {

    @Autowired
    private WordRepository wordRepository;

    public List<Word> getAll(){
        return wordRepository.findAll();
    }
    public Word getById(Long id){
        return wordRepository.findById(id).get();
    }

    public void add(Word word){
        wordRepository.save(word);
    }
}
