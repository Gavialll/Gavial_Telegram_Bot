package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.User;
import com.bot.gavial_bot.entity.Word;
import com.bot.gavial_bot.service.UserService;
import com.bot.gavial_bot.service.WordService;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

public class SprintQuiz {

    public void start(Bot bot, Update update, UserService userService, WordService sentenceService) throws TelegramApiException {
        User user = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        if(update.hasMessage()) {
            int score = userService.getById(Long.parseLong(bot.getCHAT_ID())).getQuiz().getScore();

            if(update.getMessage().getText().toLowerCase(Locale.ROOT).equals(sentenceService.getById(user.getQuiz().getQuestionId()).getEnglish())) {
                score++;
                user.getQuiz().setScore(score);
                bot.execute(EditMessageText
                        .builder()
                        .chatId(bot.getCHAT_ID())
                        .text("«" + update.getMessage().getText().toUpperCase(Locale.ROOT) + "» ✅")
                        .messageId(user.getQuiz().getMessageId())
                        .build());
            } else {
                if(user.getQuiz().getSprintMaxScore() < score) user.getQuiz().setSprintMaxScore(score);
                new Send(bot).message(Message.printResult(score, sentenceService.getById(user.getQuiz().getQuestionId()).getEnglish(), user.getQuiz().getSprintMaxScore()));
                new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SPRINT, Button.FINISH);
                user.getQuiz().clearFields();
                userService.save(user);
                return;
            }
        }

        Integer iterator = user.getQuiz().getIterator();
        List<Word> sentenceList = sentenceService.getAll();
        Integer random = Random.random(0, sentenceList.size() -1);
        Word sentence = sentenceList.get(random);

        Integer id = new Send(bot).message(Message.printQuestion(sentence.getUkraine()));
        user.getQuiz().setMessageId(id);
        user.getQuiz().setQuestionId(sentence.getId());
        iterator++;
        user.getQuiz().setIterator(iterator);

        userService.save(user);
    }
}
