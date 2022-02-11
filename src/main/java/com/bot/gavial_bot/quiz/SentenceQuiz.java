package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Sentence;
import com.bot.gavial_bot.entity.User;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

public class SentenceQuiz {
    public void start(Bot bot, Update update, SentenceService sentenceService, UserService userService) throws TelegramApiException {
        User user = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        if(update.hasMessage()) {
            int score = userService.getById(Long.parseLong(bot.getCHAT_ID())).getQuiz().getScore();
            int questionsSize = 5;
            if(update.getMessage().getText().toLowerCase(Locale.ROOT).equals(sentenceService.getById(user.getQuiz().getQuestionId()).getEnglish())) {
                score++;
                user.getQuiz().setScore(score);
                new Send(bot).message(Message.answer(user.getQuiz().getScore(), user.getQuiz().getIterator(), questionsSize));
                if(user.getQuiz().getIterator() >= questionsSize) {
                    new Sticker(bot).send(score);
                    new Send(bot).message(Message.printResult(score));
                    new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SENTENCES, Button.FINISH);
                    user.getQuiz().clearFields();
                    userService.save(user);
                    return;
                }
            } else {
                new Send(bot).message(Message.answer(user.getQuiz().getIterator(), questionsSize, sentenceService.getById(user.getQuiz().getQuestionId()).getEnglish()));
                if(user.getQuiz().getIterator() >= questionsSize) {
                    new Sticker(bot).send(score);
                    new Send(bot).message(Message.printResult(score));
                    new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SENTENCES, Button.FINISH);
                    user.getQuiz().clearFields();
                    userService.save(user);
                    return;
                }
            }
        }

        Integer iterator = user.getQuiz().getIterator();
        List<Sentence> sentenceList = sentenceService.getAll();
        Integer random = Random.random(0, sentenceList.size() -1);
        Sentence sentence = sentenceList.get(random);

        new Send(bot).message(Message.printQuestion(sentence.getUkraine()));
        user.getQuiz().setQuestionId(sentence.getId());
        iterator++;
        user.getQuiz().setIterator(iterator);

        userService.save(user);
    }
}
