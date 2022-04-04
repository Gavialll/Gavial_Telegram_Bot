package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.quiz.*;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SelectActions {
    private Bot bot;
    private Update update;

    public void select(Person person) throws TelegramApiException {
        if(person.getQuiz().getQuizStatusChoose()){
            new ChooseQuiz().start(bot, update, bot.getSentenceService(), bot.getUserService());
        }
        if(person.getQuiz().getQuizStatusSentences()) {
            new SentenceQuiz().start(bot, update, bot.getSentenceService(), bot.getUserService());
        }
        if(person.getQuiz().getQuizStatusWords()) {
            new WordsQuiz().start(bot, update, bot.getWordService(), bot.getUserService());
        }
        if(person.getQuiz().getQuizStatusSprint()){
            new SprintQuiz().start(bot, update, bot.getUserService(), bot.getWordService());
        }
        if(person.getQuiz().getQuizStatusIrregularVerb()){
            new IrregularVerbQuiz().start(bot, update, bot.getUserService(), bot.getIrregularVerbService());
        }
    }

    public SelectActions(Bot bot, Update update) {
        this.bot = bot;
        this.update = update;
    }
}
