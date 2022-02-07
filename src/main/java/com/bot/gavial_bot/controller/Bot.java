package com.bot.gavial_bot.controller;

import com.bot.gavial_bot.component.Keyboard;
import com.bot.gavial_bot.component.Message;
import com.bot.gavial_bot.component.StartPoint;
import com.bot.gavial_bot.component.Sticker;
import com.bot.gavial_bot.model.Quiz;
import com.bot.gavial_bot.model.User;
import com.bot.gavial_bot.service.QuizService;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import com.bot.gavial_bot.service.WordService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private SentenceService sentenceService;
    @Autowired
    private WordService wordService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private UserService userService;
    private String CHAT_ID;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().getText().equals("/start")) {
            CHAT_ID = update.getMessage().getChatId().toString();
            new Sticker(Bot.this).send("16");
            Long chatId = update.getMessage().getChatId();
            if(!userService.hasUser(chatId)) {
                userService.save(new User(chatId, new Quiz().clearFields()));
            } else {
                User user = userService.getById(Long.parseLong(CHAT_ID));
                user.getQuiz().clearFields();
                userService.save(user);
            }
            new Keyboard(Bot.this).printButton(Message.hello, StartPoint.STUDY_SENTENCES, StartPoint.STUDY_WORDS);
        }

        if(update.hasMessage()){
            CHAT_ID = update.getMessage().getChatId().toString();
        }
        if(update.hasCallbackQuery()) {
            CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();

            if(update.getCallbackQuery().getData().equals(StartPoint.STUDY_SENTENCES.getCallbackData())) {
                User user = userService.getById(Long.parseLong(CHAT_ID));
                user.getQuiz().clearFields();
                user.getQuiz().setQuizStatusSentences(true);
                userService.save(user);
            }
            if(update.getCallbackQuery().getData().equals(StartPoint.STUDY_WORDS.getCallbackData())) {
                User user = userService.getById(Long.parseLong(CHAT_ID));
                user.getQuiz().clearFields();
                user.getQuiz().setQuizStatusWords(true);
                userService.save(user);
            }
        }

        User user = userService.getById(Long.parseLong(CHAT_ID));

        if(user.getQuiz().getQuizStatusSentences()) {
            new Quiz().sentences(Bot.this, update, sentenceService, userService);
        }
        if(user.getQuiz().getQuizStatusWords()) {
            new Quiz().words(Bot.this, update, wordService, userService);
        }
    }

    public String getCHAT_ID() {
        return CHAT_ID;
    }

    @Override
    public String getBotUsername() {
        return "Gavial_Bot";
    }

    @Override
    public String getBotToken() {
        return "5090214861:AAEMtwQNUZm_u8ainDOmm-18Ef9aSnyGOrE";
    }
}