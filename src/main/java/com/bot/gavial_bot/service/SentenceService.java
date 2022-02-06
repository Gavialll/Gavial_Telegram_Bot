package com.bot.gavial_bot.service;

import com.bot.gavial_bot.model.Sentence;
import com.bot.gavial_bot.repository.SentenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SentenceService {

    @Autowired
    private SentenceRepository sentenceRepository;

    public List<Sentence> getAll(){
        return sentenceRepository.findAll();
    }
    public Sentence getById(Long id){
        return sentenceRepository.findById(id).get();
    }
    public void add(Sentence sentence){
        sentenceRepository.save(sentence);
    }
}
