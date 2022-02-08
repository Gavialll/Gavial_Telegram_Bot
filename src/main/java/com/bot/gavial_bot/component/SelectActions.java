package com.bot.gavial_bot.component;

import com.bot.gavial_bot.controller.Bot;
import com.bot.gavial_bot.entity.User;
import com.bot.gavial_bot.quiz.IrregularVerbQuiz;
import com.bot.gavial_bot.quiz.SentenceQuiz;
import com.bot.gavial_bot.quiz.SprintQuiz;
import com.bot.gavial_bot.quiz.WordsQuiz;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class SelectActions {
    private Bot bot;
    private Update update;

    public void select(User user) throws TelegramApiException {
        if(user.getQuiz().getQuizStatusSentences()) {
            new SentenceQuiz().start(bot, update, bot.getSentenceService(), bot.getUserService());
        }
        if(user.getQuiz().getQuizStatusWords()) {
            new WordsQuiz().start(bot, update, bot.getWordService(), bot.getUserService());
        }
        if(user.getQuiz().getQuizStatusSprint()){
            new SprintQuiz().start(bot, update, bot.getUserService(), bot.getWordService());
        }
        if(user.getQuiz().getQuizStatusIrregularVerb()){
            new IrregularVerbQuiz().start(bot, update, bot.getUserService(), bot.getIrregularVerbService());
        }
    }

    public SelectActions(Bot bot, Update update) {
        this.bot = bot;
        this.update = update;
    }
}
