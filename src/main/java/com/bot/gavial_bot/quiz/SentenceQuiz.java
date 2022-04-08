package com.bot.gavial_bot.quiz;

import com.bot.gavial_bot.component.*;
import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.entity.Sentence;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Locale;

@Slf4j
public class SentenceQuiz {
    public void start(Bot bot, Update update, SentenceService sentenceService, UserService userService) throws TelegramApiException {
        Person person = userService.getById(Long.parseLong(bot.getCHAT_ID()));
        Integer iterator = person.getQuiz().getIterator();
        if(update.hasMessage()) {
            int score = userService.getById(Long.parseLong(bot.getCHAT_ID())).getQuiz().getScore();
            int questionsSize = 10;

            String userAnswer = update.getMessage().getText().toLowerCase(Locale.ROOT);
            String rightAnswer = sentenceService.getById(person.getQuiz().getQuestionId()).getEnglish().toLowerCase(Locale.ROOT);

            if(userAnswer.equals(rightAnswer)) {
                log.info("Print -> next Sentence: " + iterator);
                score++;
                person.getQuiz().setScore(score);
                new Send(bot).message(Message.answer(person.getQuiz().getScore(), person.getQuiz().getIterator(), questionsSize));
                if(finishSentenceQuiz(bot, userService, person, score, questionsSize)) return;
            } else {
                log.info("Finish -> Sentence");
                new Send(bot).message(Message.answer(person.getQuiz().getIterator(), questionsSize, rightAnswer));
                if(finishSentenceQuiz(bot, userService, person, score, questionsSize)) return;
            }
        }

        List<Sentence> sentenceList = sentenceService.getAll();
        Integer random = Random.random(0, sentenceList.size() -1);
        Sentence sentence = sentenceList.get(random);

        new Send(bot).message(Message.printQuestion(sentence.getUkraine()));
        person.getQuiz().setQuestionId(sentence.getId());
        iterator++;
        person.getQuiz().setIterator(iterator);

        userService.save(person);
        log.info("Print -> first Sentence: " + iterator);
    }

    private boolean finishSentenceQuiz(Bot bot, UserService userService, Person person, int score, int questionsSize) throws TelegramApiException {
        if(person.getQuiz().getIterator() >= questionsSize) {
            new Sticker(bot).send(score);
            new Send(bot).message(Message.printResult(score));
            new Keyboard(bot).printButton(Message.selectActive, Button.TRY_AGAIN_SENTENCES, Button.FINISH);
            person.getQuiz().clearFields();
            userService.save(person);
            return true;
        }
        return false;
    }
}
