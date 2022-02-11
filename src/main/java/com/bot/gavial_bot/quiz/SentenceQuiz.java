package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.entity.Sentence;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

public class SentenceQuiz {
    public void start(Bot bot, Update update, SentenceService sentenceService, UserService userService) throws TelegramApiException {
        Person person = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        if(update.hasMessage()) {
            int score = userService.getById(Long.parseLong(bot.getCHAT_ID())).getQuiz().getScore();
            int questionsSize = 5;
            if(update.getMessage().getText().toLowerCase(Locale.ROOT).equals(sentenceService.getById(person.getQuiz().getQuestionId()).getEnglish())) {
                score++;
                person.getQuiz().setScore(score);
                new Send(bot).message(Message.answer(person.getQuiz().getScore(), person.getQuiz().getIterator(), questionsSize));
                if(person.getQuiz().getIterator() >= questionsSize) {
                    new Sticker(bot).send(score);
                    new Send(bot).message(Message.printResult(score));
                    new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SENTENCES, Button.FINISH);
                    person.getQuiz().clearFields();
                    userService.save(person);
                    return;
                }
            } else {
                new Send(bot).message(Message.answer(person.getQuiz().getIterator(), questionsSize, sentenceService.getById(person.getQuiz().getQuestionId()).getEnglish()));
                if(person.getQuiz().getIterator() >= questionsSize) {
                    new Sticker(bot).send(score);
                    new Send(bot).message(Message.printResult(score));
                    new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SENTENCES, Button.FINISH);
                    person.getQuiz().clearFields();
                    userService.save(person);
                    return;
                }
            }
        }

        Integer iterator = person.getQuiz().getIterator();
        List<Sentence> sentenceList = sentenceService.getAll();
        Integer random = Random.random(0, sentenceList.size() -1);
        Sentence sentence = sentenceList.get(random);

        new Send(bot).message(Message.printQuestion(sentence.getUkraine()));
        person.getQuiz().setQuestionId(sentence.getId());
        iterator++;
        person.getQuiz().setIterator(iterator);

        userService.save(person);
    }
}
