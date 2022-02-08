package com.bot.gavial_bot.service;

import com.bot.gavial_bot.entity.IrregularVerb;
import com.bot.gavial_bot.repository.IrregularVerbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IrregularVerbService {
    @Autowired
    private IrregularVerbRepository irregularVerbRepository;

    public IrregularVerb get(Long id) {
        return irregularVerbRepository.findById(id).get();
    }

    public List<IrregularVerb> getAll(){
        return irregularVerbRepository.findAll();
    }
}
