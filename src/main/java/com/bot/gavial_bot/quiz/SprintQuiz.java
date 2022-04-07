package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Person;
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
        Person person = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        if(update.hasMessage()) {
            int score = person.getQuiz().getScore();

            String userAnswer = update.getMessage().getText().toLowerCase(Locale.ROOT);
            String rightAnswer = sentenceService.getById(person.getQuiz().getQuestionId()).getEnglish().toLowerCase(Locale.ROOT);

            if(userAnswer.equals(rightAnswer)) {
                score++;
                person.getQuiz().setScore(score);
                bot.execute(EditMessageText
                        .builder()
                        .chatId(bot.getCHAT_ID())
                        .text("«" + update.getMessage().getText().toUpperCase(Locale.ROOT) + "» ✅")
                        .messageId(person.getQuiz().getMessageId())
                        .build());
            } else {
                if(person.getQuiz().getSprintMaxScore() < score) person.getQuiz().setSprintMaxScore(score);
                new Send(bot).message(Message.printResult(score, rightAnswer.toUpperCase(Locale.ROOT), person.getQuiz().getSprintMaxScore()));
                new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SPRINT, Button.FINISH);
                person.getQuiz().clearFields();
                userService.save(person);
                return;
            }
        }

        List<Word> sentenceList = sentenceService.getAll();
        Integer random = Random.random(0, sentenceList.size() -1);
        Word sentence = sentenceList.get(random);

        Integer id = new Send(bot).message(Message.printQuestion(sentence.getUkraine()));
        person.getQuiz().setMessageId(id);
        person.getQuiz().setQuestionId(sentence.getId());

        userService.save(person);
    }
}
