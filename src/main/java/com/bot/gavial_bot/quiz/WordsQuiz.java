package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.User;
import com.bot.gavial_bot.entity.Word;
import com.bot.gavial_bot.service.UserService;
import com.bot.gavial_bot.service.WordService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

public class WordsQuiz {
    public void start(Bot bot, Update update, WordService wordService, UserService userService) throws TelegramApiException {
        User user = userService.getById(Long.parseLong(bot.getCHAT_ID()));

        if(update.hasMessage()) {
            int score = userService.getById(Long.parseLong(bot.getCHAT_ID())).getQuiz().getScore();
            int questionsSize = 15;
            if(update.getMessage().getText().toLowerCase(Locale.ROOT).equals(wordService.getById(user.getQuiz().getQuestionId()).getEnglish())) {
                score++;
                user.getQuiz().setScore(score);
                new Send(bot).message(Message.answer(user.getQuiz().getScore(), user.getQuiz().getIterator(), questionsSize));
                if(user.getQuiz().getIterator() >= questionsSize) {
                    new Sticker(bot).send(score);
                    new Send(bot).message(Message.printResult(score));
                    new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_WORDS, Button.FINISH);
                    user.getQuiz().clearFields();
                    userService.save(user);
                    return;
                }
            } else {
                new Send(bot).message(Message.answer(user.getQuiz().getIterator(), questionsSize, wordService.getById(user.getQuiz().getQuestionId()).getEnglish()));
                if(user.getQuiz().getIterator() >= questionsSize) {
                    new Sticker(bot).send(score);
                    new Send(bot).message(Message.printResult(score));
                    new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_WORDS, Button.FINISH);
                    user.getQuiz().clearFields();
                    userService.save(user);
                    return;
                }
            }
        }

        Integer iterator = user.getQuiz().getIterator();
        List<Word> sentenceList = wordService.getAll();
        Integer random = Random.random(0, sentenceList.size() -1);
        Word word = sentenceList.get(random);

        new Send(bot).message(Message.printQuestion(word.getUkraine()));
        user.getQuiz().setQuestionId(word.getId());
        iterator++;
        user.getQuiz().setIterator(iterator);
        userService.save(user);
    }
}
