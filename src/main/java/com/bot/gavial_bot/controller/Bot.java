package com.bot.gavial_bot.controller;

import com.bot.gavial_bot.component.Keyboard;
import com.bot.gavial_bot.component.SelectActions;
import com.bot.gavial_bot.component.Sticker;
import com.bot.gavial_bot.entity.Person;
import com.bot.gavial_bot.entity.Quiz;
import com.bot.gavial_bot.service.IrregularVerbService;
import com.bot.gavial_bot.service.SentenceService;
import com.bot.gavial_bot.service.UserService;
import com.bot.gavial_bot.service.WordService;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Component
@Data
public class Bot extends TelegramLongPollingBot {
    @Autowired
    private SentenceService sentenceService;
    @Autowired
    private IrregularVerbService irregularVerbService;
    @Autowired
    private WordService wordService;
    @Autowired
    private UserService userService;
    private String CHAT_ID;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().getText().equals("/start")) {
            CHAT_ID = update.getMessage().getChatId().toString();
            startBot(update);
        }
        if(update.hasMessage()){
            CHAT_ID = update.getMessage().getChatId().toString();
        }

        if(update.hasCallbackQuery()) {
            CHAT_ID = update.getCallbackQuery().getMessage().getChatId().toString();
            Person person = userService.getById(Long.parseLong(CHAT_ID));
            person.getQuiz().clearFields();

            switch(update.getCallbackQuery().getData()){
                case "/studyWords": {
                    person.getQuiz().setQuizStatusWords(true);
                    break;
                }
                case "/irregularVerb": {
                    person.getQuiz().setQuizStatusIrregularVerb(true);
                    break;
                }
                case "/studySentence": {
                    person.getQuiz().setQuizStatusSentences(true);
                    break;
                }
                case "/studySprint": {
                    person.getQuiz().setQuizStatusSprint(true);
                    break;
                }
                case "/finish": {
                    person.getQuiz().clearFields();
                    new Keyboard(Bot.this).printMenu();
                    break;
                }
            }
            userService.save(person);
        }

        Person person = userService.getById(Long.parseLong(CHAT_ID));

        new SelectActions(Bot.this, update).select(person);

    }

    private void startBot(Update update) throws TelegramApiException {
        new Sticker(Bot.this).send("16");
        Long chatId = update.getMessage().getChatId();
        if(!userService.hasUser(chatId)) {
            userService.save(new Person(chatId, new Quiz().clearFields()));
        } else {
            Person person = userService.getById(Long.parseLong(CHAT_ID));
            person.getQuiz().clearFields();
            userService.save(person);
        }
        new Keyboard(Bot.this).printMenu();
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