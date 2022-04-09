package com.bot.gavial_bot.controller;

import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.quiz.*;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
public class SelectActions {
    private final Bot bot;
    private final Update update;

    public void select(Person person) throws TelegramApiException {
        if(person.getQuiz().getQuizStatusChoose()){
            log.info("Start -> Choose quiz");
            new ChooseQuiz().start(bot, update, bot.getSentenceService(), bot.getUserService());
        }
        if(person.getQuiz().getQuizStatusSentences()) {
            log.info("Start -> Sentence quiz");
            new SentenceQuiz().start(bot, update, bot.getSentenceService(), bot.getUserService());
        }
        if(person.getQuiz().getQuizStatusWords()) {
            log.info("Start -> Word quiz");
            new WordsQuiz().start(bot, update, bot.getWordService(), bot.getUserService());
        }
        if(person.getQuiz().getQuizStatusSprint()){
            log.info("Start -> Sprint quiz");
            new SprintQuiz().start(bot, update, bot.getUserService(), bot.getWordService());
        }
        if(person.getQuiz().getQuizStatusIrregularVerb()){
            log.info("Start -> Irregular verb quiz");
            new IrregularVerbQuiz().start(bot, update, bot.getUserService(), bot.getIrregularVerbService());
        }
    }

    public SelectActions(Bot bot, Update update) {
        this.bot = bot;
        this.update = update;
    }
}
