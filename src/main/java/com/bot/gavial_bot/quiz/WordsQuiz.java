package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.entity.Word;
import com.bot.gavial_bot.service.UserService;
import com.bot.gavial_bot.service.WordService;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

public class WordsQuiz {
    public void start(Bot bot, Update update, WordService wordService, UserService userService) throws TelegramApiException {
        Person person = userService.getById(Long.parseLong(bot.getCHAT_ID()));

        if(update.hasMessage()) {
            int score = person.getQuiz().getScore();
            int questionsSize = 10;

            String userAnswer = update.getMessage().getText().toLowerCase(Locale.ROOT);
            String rightAnswer = wordService.getById(person.getQuiz().getQuestionId()).getEnglish().toLowerCase(Locale.ROOT);

            if(userAnswer.equals(rightAnswer)) {
                score++;
                person.getQuiz().setScore(score);

                new Send(bot).message(Message.answer(person.getQuiz().getScore(), person.getQuiz().getIterator(), questionsSize));
                if(finishWordQuiz(bot, userService, person, score, questionsSize)) return;
            } else {
                new Send(bot).message(Message.answer(person.getQuiz().getIterator(), questionsSize, rightAnswer));
                if(finishWordQuiz(bot, userService, person, score, questionsSize)) return;
            }
        }

        Integer iterator = person.getQuiz().getIterator();
        List<Word> sentenceList = wordService.getAll();
        Integer random = Random.random(0, sentenceList.size() - 1);
        Word word = sentenceList.get(random);

        new Send(bot).message(Message.printQuestion(word.getUkraine()));
        person.getQuiz().setQuestionId(word.getId());
        iterator++;
        person.getQuiz().setIterator(iterator);
        userService.save(person);
    }

    private boolean finishWordQuiz(Bot bot, UserService userService, Person person, int score, int questionsSize) throws TelegramApiException {
        if(person.getQuiz().getIterator() >= questionsSize) {
            new Sticker(bot).send(score);
            new Send(bot).message(Message.printResult(score));
            new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_WORDS, Button.FINISH);
            person.getQuiz().clearFields();
            userService.save(person);
            return true;
        }
        return false;
    }
}
